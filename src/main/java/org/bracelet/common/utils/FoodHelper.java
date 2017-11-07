package org.bracelet.common.utils;

import org.bracelet.common.bp.NeuronNet;
import org.bracelet.common.enums.ActivityType;
import org.bracelet.common.enums.Sex;
import org.bracelet.entity.Food;
import org.bracelet.entity.Recipe;
import org.bracelet.entity.SleepState;
import org.bracelet.entity.SportState;

import java.util.*;

public class FoodHelper {
    public static final Sex MALE = Sex.MALE;
    public static final Sex FEMALE = Sex.FEMALE;
    private static final int MINUTES_OF_ONE_DAY = 24 * 60;

    /**
     * 计算基础代谢率（单位：千卡）: 基础代谢率(BMR)是指人体在 非活动状态 下，维持生命所需的最低能量。
     * 这些能量主要用于维持人体各器官的机能，如呼吸、心跳、氧气运送、腺体分泌、排泄等等。
     * 适量运动会提高人体的基础代谢率，而节食则会降低基础代谢率。
     * @param sex 性别
     * @param weight 体重（kg）
     * @param height 身高（cm）
     * @param age 年龄
     * @return BMR
     */
    public static int calBMR(Sex sex, double weight, double height, int age) {
        int result;
        if (sex.equals(MALE)) {
            result = (int)Math.round(67 + (13.73 * weight) + (5.0 * height) - (6.9 * age));
        } else if (sex.equals(FEMALE)){
            result = (int)Math.round(661 + (9.6 * weight) + (1.72 * height) - (4.7 * age));
        } else {
            result = (int)Math.round(((66 + (13.7 * weight) + (5.0 * height) - (6.8 * age)) + (655 + (9.6 * weight) + (1.7 * height) - (4.7 * age))) / 2);
        }
        return result;
    }

    /**
     * 计算理想体重
     * @param sex 性别
     * @param height 身高（cm）
     * @return 理想体重
     */
    public static int calLegendWeight(Sex sex, double height) {
        int result;
        if (sex.equals(MALE)) {
            result = (int)Math.round(50 + (2.3 * (height - 152)) / 2.54);
        } else if (sex.equals(FEMALE)) {
            result = (int)Math.round(45.5 + (2.3 * (height - 152)) / 2.54);
        } else {
            result = (int)Math.round(((50 + (2.3 * (height - 152)) / 2.54) + (45.5 + (2.3 * (height - 152)) / 2.54)) / 2);
        }
        return result;
    }

    /**
     * 计算BMI
     * @param weight 体重（kg）
     * @param height 身高（cm）
     * @return BMI
     */
    public static int calBMI(double weight, double height) {
        return (int)Math.round(weight * 10000 / height * height);
    }

    /**
     * 计算活动消耗卡路里
     * @param weight 体重（kg）
     * @param minutes 时间（分钟）
     * @param activityType 活动类型
     * @return 进行活动minutes分钟所需要的卡路里（单位：千卡）
     */
    public static int calActivityCalories(double weight, int minutes, ActivityType activityType) {
        return (int)Math.round(((minutes / 60) * (weight / 100)) * activityType.getKcal());
    }

    /**
     * 计算总的能量消耗
     * @param weight
     * @param height
     * @param age
     * @param sex
     * @param sportLists
     * @param sleepLists
     * @return
     */
    public static List<Integer> calAllCalories(double weight, double height, int age, Sex sex,
                                               List<List<SportState>> sportLists, List<List<SleepState>> sleepLists) {
        List<Integer> results = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int calories = calBMR(sex, weight, height, age);
            int activityMinute = 0;
            for (SportState sportState : sportLists.get(i)) {
                int minutes = Math.round((sportState.getEndTime().getTime() - sportState.getStartTime().getTime()) / 1000 / 60);
                double speed = sportState.getKilometre() / minutes;
                ActivityType activityType;
                if ("walk".equals(sportState.getSportType())) {
                    activityType = ActivityType.getWalk(speed);
                } else if ("run".equals(sportState.getSportType())) {
                    if (minutes <= 10) {
                        activityType = ActivityType.WalkAndRun;
                    } else {
                        activityType = ActivityType.getRun(speed);
                    }
                } else {
                    activityType = ActivityType.StandOnly;
                }
                activityMinute += minutes;
                calories += calActivityCalories(weight, minutes, activityType);
            }
            for (SleepState sleepState : sleepLists.get(i)) {
                int minutes = Math.round((sleepState.getEndTime().getTime() - sleepState.getStartTime().getTime()) / 1000 / 60);
                activityMinute += minutes;
                calories += calActivityCalories(weight, minutes, ActivityType.Sleep);
            }
            int surplusMinutes = MINUTES_OF_ONE_DAY - activityMinute;
            if (surplusMinutes > 0) {
                calories += calActivityCalories(weight, surplusMinutes, ActivityType.SitForWork);
            }
            results.add(calories);
        }
        return results;
    }

    /**
     * 获取能量摄入量
     * @param calories
     * @return
     */
    public static int calNeedCalories(int calories) {
        return (int)(Math.round(1.1 * calories));
    }

    /**
     * 预测能量消耗
     * @param weight
     * @param height
     * @param age
     * @param sex
     * @param caloriesList
     * @return
     */
    public static int forecastCalories(double weight, double height, int age, Sex sex, List<Integer> caloriesList) {
        byte[] weightBytes = ByteUtils.shortToByte2(Double.valueOf(weight).shortValue());  // 16
        byte[] heightBytes = ByteUtils.shortToByte2(Double.valueOf(height).shortValue());  // 16
        byte[] ageBytes = ByteUtils.shortToByte2(Integer.valueOf(age).shortValue());    // 16
        byte sexBytes = sex.equals(MALE) ? 0 : Byte.MIN_VALUE; // 8
        double[][] input = new double[7][64];
        double[][] output = new double[7][32];
        // 隐含层节点数 = sqrt(输入层*输出层)
        NeuronNet bp = new NeuronNet(new int[] {64, (int)Math.round(Math.sqrt(64.0*32.0)), 32});
        for (int i = 0; i < input.length; i++) {
            // set input[i]
            byte[] byte8 = { weightBytes[0], weightBytes[1], heightBytes[0],
                    heightBytes[1], ageBytes[0], ageBytes[1], sexBytes, (byte)i };
            byte[] byte64 = ByteUtils.longToByte64(ByteUtils.byte8ToLong(byte8));
            for (int j = 0; j < input[i].length; j++) {
                input[i][j] = byte64[j];
            }

            // set output[i]
            byte[] byte32 = ByteUtils.intToByte32(caloriesList.get(i));
            for (int j = 0; j < output[i].length; j++) {
                output[i][j] = byte32[j];
            }
        }

        bp.train(input, output);

        byte[] byte8 = { weightBytes[0], weightBytes[1], heightBytes[0],
                heightBytes[1], ageBytes[0], ageBytes[1], sexBytes, (byte)input.length };
        byte[] byte64 = ByteUtils.longToByte64(ByteUtils.byte8ToLong(byte8));
        double[] predictValue = new double[64];
        for (int i = 0; i < predictValue.length; i++) {
            predictValue[i] = byte64[i];
        }
        double[] resultValue = bp.predict(predictValue);
        byte[] byte32 = new byte[32];
        for (int i = 0; i < resultValue.length; i++) {
            byte32[i] = (byte)Math.round(resultValue[i]);
        }
        return ByteUtils.byte32ToInt(byte32);
    }

    public static Recipe makeRecipe(List<Food> foods, int calories, int age, Sex sex) {
        Recipe recipe = new Recipe();
        double protein = getProteinDRI(sex, age);
        double carbohyd = getCarbohydDRI(age);
        double calcium = getCalciumDRI(age);
        double phosphorus = getPhosphorusDRI(age);
        double potassium = getPotassiumDRI(age);
        double sodium = getSodiumDRI(age);
        double magnesium = getMagnesiumDRI(age);
        double iron = getIronDRI(sex, age);
        double zinc = getZincDRI(sex, age);
        double selenium = getSeleniumDRI(age);
        double copper = getCopperDRI(age);
        double manganese = getManganeseDRI(age);
        double vitaminC = getVitaminCDRI(age);
        double water = getWaterDRI(sex, age);
        recipe.setWater(water);
        Food
                fHea = null, sHea = null,
                fPro = null, sPro = null,
                fCar = null, sCar = null,
                fCal = null, sCal = null,
                fPho = null, sPho = null,
                fPot = null, sPot = null,
                fSod = null, sSod = null,
                fMag = null, sMag = null,
                fIro = null, sIro = null,
                fZin = null, sZin = null,
                fSel = null, sSel = null,
                fCop = null, sCop = null,
                fMan = null, sMan = null,
                fVit = null, sVit = null;
        Map<String, Food> foodMap = new HashMap<>();
        for (Food food : foods) {
            if (fHea == null || food.getHeatContent() > fHea.getHeatContent()) {
                sHea = fHea;
                foodMap.put("sHea", sHea);
                fHea = food;
                foodMap.put("fHea", fHea);
            } else if (sHea == null || food.getHeatContent() > sHea.getHeatContent()) {
                sHea = food;
                foodMap.put("sHea", sHea);
            }

            if (fPro == null || food.getProtein() > fPro.getProtein()) {
                sPro = fPro;
                foodMap.put("sPro", sPro);
                fPro = food;
                foodMap.put("fPro", fPro);
            } else if (sPro == null || food.getProtein() > sPro.getProtein()) {
                sPro = food;
                foodMap.put("sPro", sPro);
            }

            if (fCar == null || food.getCarbohydrate() > fCar.getCarbohydrate()) {
                sCar = fCar;
                foodMap.put("sCar", sCar);
                fCar = food;
                foodMap.put("fCar", fCar);
            } else if (sCar == null || food.getCarbohydrate() > sCar.getCarbohydrate()) {
                sCar = food;
                foodMap.put("sCar", sCar);
            }

            if (fCal == null || food.getCalcium() > fCal.getCalcium()) {
                sCal = fCal;
                foodMap.put("sCal", sCal);
                fCal = food;
                foodMap.put("fCal", fCal);
            } else if (sCal == null || food.getCalcium() > sCal.getCalcium()) {
                sCal = food;
                foodMap.put("sCal", sCal);
            }

            if (fPho == null || food.getPhosphorus() > fPho.getPhosphorus()) {
                sPho = fPho;
                foodMap.put("sPho", sPho);
                fPho = food;
                foodMap.put("fPho", fPho);
            } else if (sPho == null || food.getPhosphorus() > sPho.getPhosphorus()) {
                sPho = food;
                foodMap.put("sPho", sPho);
            }

            if (fPot == null || food.getPotassium() > fPot.getPotassium()) {
                sPot = fPot;
                foodMap.put("sPot", sPot);
                fPot = food;
                foodMap.put("fPot", fPot);
            } else if (sPot == null || food.getPotassium() > sPot.getPotassium()) {
                sPot = food;
                foodMap.put("sPot", sPot);
            }

            if (fSod == null || food.getSodium() > fSod.getSodium()) {
                sSod = fSod;
                foodMap.put("sSod", sSod);
                fSod = food;
                foodMap.put("fSod", fSod);
            } else if (sSod == null || food.getSodium() > sSod.getSodium()) {
                sSod = food;
                foodMap.put("sSod", sSod);
            }

            if (fMag == null || food.getMagnesium() > fMag.getMagnesium()) {
                sMag = fMag;
                foodMap.put("sMag", sMag);
                fMag = food;
                foodMap.put("fMag", fMag);
            } else if (sMag == null || food.getMagnesium() > sMag.getMagnesium()) {
                sMag = food;
                foodMap.put("sMag", sMag);
            }

            if (fIro == null || food.getIron() > fIro.getIron()) {
                sIro = fIro;
                foodMap.put("sIro", sIro);
                fIro = food;
                foodMap.put("fIro", fIro);
            } else if (sIro == null || food.getIron() > sIro.getIron()) {
                sIro = food;
                foodMap.put("sIro", sIro);
            }

            if (fZin == null || food.getZinc() > fZin.getZinc()) {
                sZin = fZin;
                foodMap.put("sZin", sZin);
                fZin = food;
                foodMap.put("fZin", fZin);
            } else if (sZin == null || food.getZinc() > sZin.getZinc()) {
                sZin = food;
                foodMap.put("sZin", sZin);
            }

            if (fSel == null || food.getSelenium() > fSel.getSelenium()) {
                sSel = fSel;
                foodMap.put("sSel", sSel);
                fSel = food;
                foodMap.put("fSel", fSel);
            } else if (sSel == null || food.getSelenium() > sSel.getSelenium()) {
                sSel = food;
                foodMap.put("sSel", sSel);
            }

            if (fCop == null || food.getCopper() > fCop.getCopper()) {
                sCop = fCop;
                foodMap.put("sCop", sCop);
                fCop = food;
                foodMap.put("fCop", fCop);
            } else if (sCop == null || food.getCopper() > sCop.getCopper()) {
                sCop = food;
                foodMap.put("sCop", sCop);
            }

            if (fMan == null || food.getManganese() > fMan.getManganese()) {
                sMan = fMan;
                foodMap.put("sMan", sMan);
                fMan = food;
                foodMap.put("fMan", fMan);
            } else if (sMan == null || food.getManganese() > sMan.getManganese()) {
                sMan = food;
                foodMap.put("sMan", sMan);
            }

            if (fVit == null || food.getVitaminC() > fVit.getVitaminC()) {
                sVit = fVit;
                foodMap.put("sVit", sVit);
                fVit = food;
                foodMap.put("fVit", fVit);
            } else if (sVit == null || food.getVitaminC() > sVit.getVitaminC()) {
                sVit = food;
                foodMap.put("sVit", sVit);
            }
        }

        // 添加至食谱
        for (Food food : foodMap.values()) {
            if (food != null) {
                recipe.addFood(food);
            }
        }

        List<String> removeFoodKeys = new ArrayList<>();
        if (recipe.getProtein() >= protein * 1.3) {
            removeFoodKeys.add("fPro");
        } else if (recipe.getProtein() >= protein * 1.15) {
            removeFoodKeys.add("sPro");
        }

        if (recipe.getCarbohydrate() >= carbohyd * 1.3) {
            removeFoodKeys.add("fCar");
        } else if (recipe.getCarbohydrate() >= carbohyd * 1.15) {
            removeFoodKeys.add("sCar");
        }

        if (recipe.getCalcium() >= calcium * 1.3) {
            removeFoodKeys.add("fCal");
        } else if (recipe.getCalcium() >= calcium * 1.15) {
            removeFoodKeys.add("sCal");
        }

        if (recipe.getPhosphorus() >= phosphorus * 1.3) {
            removeFoodKeys.add("fPho");
        } else if (recipe.getPhosphorus() >= phosphorus * 1.15) {
            removeFoodKeys.add("sPho");
        }

        if (recipe.getPotassium() >= potassium * 1.3) {
            removeFoodKeys.add("fPot");
        } else if (recipe.getPotassium() >= potassium * 1.15) {
            removeFoodKeys.add("sPot");
        }

        if (recipe.getSodium() >= sodium * 1.3) {
            removeFoodKeys.add("fSod");
        } else if (recipe.getSodium() >= sodium * 1.15) {
            removeFoodKeys.add("sSod");
        }

        if (recipe.getMagnesium() >= magnesium * 1.3) {
            removeFoodKeys.add("fMag");
        } else if (recipe.getMagnesium() >= magnesium * 1.15) {
            removeFoodKeys.add("sMag");
        }

        if (recipe.getIron() >= iron * 1.3) {
            removeFoodKeys.add("fIro");
        } else if (recipe.getIron() >= iron * 1.15) {
            removeFoodKeys.add("sIro");
        }

        if (recipe.getZinc() >= zinc * 1.3) {
            removeFoodKeys.add("fZin");
        } else if (recipe.getZinc() >= zinc * 1.15) {
            removeFoodKeys.add("sZin");
        }

        if (recipe.getSelenium() >= selenium * 1.3) {
            removeFoodKeys.add("fSel");
        } else if (recipe.getSelenium() >= selenium * 1.15) {
            removeFoodKeys.add("sSel");
        }

        if (recipe.getCopper() >= copper * 1.3) {
            removeFoodKeys.add("fCop");
        } else if (recipe.getCopper() >= copper * 1.15) {
            removeFoodKeys.add("sCop");
        }

        if (recipe.getManganese() >= manganese * 1.3) {
            removeFoodKeys.add("fMan");
        } else if (recipe.getManganese() >= manganese * 1.15) {
            removeFoodKeys.add("sMan");
        }

        if (recipe.getVitaminC() >= vitaminC * 1.3) {
            removeFoodKeys.add("fVit");
        } else if (recipe.getVitaminC() >= vitaminC * 1.15) {
            removeFoodKeys.add("sVit");
        }

        boolean flag = true;
        for (int i = 0; i < removeFoodKeys.size() && flag; i++) {
            recipe.delFood(foodMap.get(removeFoodKeys.get(i)));
            flag = recipe.getProtein() <= protein * 1.1
                    && recipe.getCarbohydrate() <= carbohyd * 1.1
                    && recipe.getCalcium() <= calcium * 1.1
                    && recipe.getPhosphorus() <= phosphorus * 1.1
                    && recipe.getPotassium() <= potassium * 1.1
                    && recipe.getSodium() <= sodium * 1.1
                    && recipe.getMagnesium() <= magnesium * 1.1
                    && recipe.getIron() <= iron * 1.1
                    && recipe.getZinc() <= zinc * 1.1
                    && recipe.getSelenium() <= selenium * 1.1
                    && recipe.getCopper() <= copper * 1.1
                    && recipe.getManganese() <= manganese * 1.1
                    && recipe.getVitaminC() <= manganese * 1.1;
        }

        int diffCalories = calories - recipe.getHeatContent();
        int count;
        Food maxHeatFood = null;
        while (diffCalories < 0) {
            count = 0;
            for (Food food : foods) {
                if (food.getHeatContent() >= diffCalories && !recipe.isExist(food)) {
                    recipe.addFood(food);
                    maxHeatFood = null;
                    count++;
                    break;
                } else if (!recipe.isExist(food) && (maxHeatFood == null ||
                        maxHeatFood.getHeatContent() < food.getHeatContent())) {
                    maxHeatFood = food;
                    count++;
                }
            }
            if (count + recipe.size() >= foods.size())
                break;
            if (maxHeatFood != null) {
                recipe.addFood(maxHeatFood);
            }
            diffCalories = calories - recipe.getHeatContent();
        }
        return recipe;
    }


    /**
     * 获取蛋白质推荐量
     * @param sex
     * @param age
     * @return
     */
    public static double getProteinDRI(Sex sex, int age) {
        double r;
        boolean f = sex.equals(MALE);
        if (age >= 18) {
            r = f ? 65 : 55;
        } else if (age >= 14) {
            r = f ? 75 : 60;
        } else if (age >= 11) {
            r = f ? 60 : 55;
        } else if (age >= 10) {
            r = 50;
        } else if (age >= 9) {
            r = 45;
        } else if (age >= 7) {
            r = 40;
        } else if (age >= 6) {
            r = 35;
        } else if (age >= 3) {
            r = 30;
        } else if (age >= 1) {
            r = 25;
        } else if (age >= 0.5) {
            r = 20;
        } else {
            r = 9;
        }
        return r;
    }

    /**
     * 获取碳水化合物推荐量
     * @param age
     * @return
     */
    public static double getCarbohydDRI(int age) {
        double r;
        if (age >= 18) {
            r = 120;
        } else if (age >= 11) {
            r = 150;
        } else if (age >= 1) {
            r = 120;
        } else if (age >= 0.5) {
            r = 85;
        } else {
            r = 60;
        }
        return r;
    }

    /**
     * 获取钙推荐量
     * @param age
     * @return
     */
    public static double getCalciumDRI(int age) {
        double r;
        if (age >= 80) {
            r = 1000;
        } else if (age >= 65) {
            r = 1000;
        } else if (age >= 50) {
            r = 1000;
        } else if (age >= 18) {
            r = 800;
        } else if (age >= 14) {
            r = 1000;
        } else if (age >= 11) {
            r = 1200;
        } else if (age >= 7) {
            r = 1000;
        } else if (age >= 4) {
            r = 800;
        } else if (age >= 1) {
            r = 600;
        } else if (age >= 0.5) {
            r = 250;
        } else {
            r = 200;
        }
        return r;
    }

    /**
     * 获取磷推荐量
     * @param age
     * @return
     */
    public static double getPhosphorusDRI(int age) {
        double r;
        if (age >= 80) {
            r = 670;
        } else if (age >= 65) {
            r = 700;
        } else if (age >= 50) {
            r = 720;
        } else if (age >= 18) {
            r = 720;
        } else if (age >= 14) {
            r = 710;
        } else if (age >= 11) {
            r = 640;
        } else if (age >= 7) {
            r = 470;
        } else if (age >= 4) {
            r = 350;
        } else if (age >= 1) {
            r = 300;
        } else if (age >= 0.5) {
            r = 180;
        } else {
            r = 100;
        }
        return r;
    }

    /**
     * 获取钾推荐量
     * @param age
     * @return
     */
    public static double getPotassiumDRI(int age) {
        double r;
        if (age >= 80) {
            r = 2000;
        } else if (age >= 65) {
            r = 2000;
        } else if (age >= 50) {
            r = 2000;
        } else if (age >= 18) {
            r = 2000;
        } else if (age >= 14) {
            r = 2200;
        } else if (age >= 11) {
            r = 1900;
        } else if (age >= 7) {
            r = 1500;
        } else if (age >= 4) {
            r = 1200;
        } else if (age >= 1) {
            r = 900;
        } else if (age >= 0.5) {
            r = 550;
        } else {
            r = 350;
        }
        return r;
    }

    /**
     * 获取钠推荐量
     * @param age
     * @return
     */
    public static double getSodiumDRI(int age) {
        double r;
        if (age >= 80) {
            r = 1300;
        } else if (age >= 65) {
            r = 1400;
        } else if (age >= 50) {
            r = 1400;
        } else if (age >= 18) {
            r = 1500;
        } else if (age >= 14) {
            r = 1600;
        } else if (age >= 11) {
            r = 1400;
        } else if (age >= 7) {
            r = 1200;
        } else if (age >= 4) {
            r = 900;
        } else if (age >= 1) {
            r = 700;
        } else if (age >= 0.5) {
            r = 350;
        } else {
            r = 170;
        }
        return r;
    }

    /**
     * 获取镁推荐量
     * @param age
     * @return
     */
    public static double getMagnesiumDRI(int age) {
        double r;
        if (age >= 80) {
            r = 310;
        } else if (age >= 65) {
            r = 320;
        } else if (age >= 50) {
            r = 330;
        } else if (age >= 18) {
            r = 330;
        } else if (age >= 14) {
            r = 320;
        } else if (age >= 11) {
            r = 300;
        } else if (age >= 7) {
            r = 220;
        } else if (age >= 4) {
            r = 160;
        } else if (age >= 1) {
            r = 140;
        } else if (age >= 0.5) {
            r = 65;
        } else {
            r = 20;
        }
        return r;
    }

    /**
     * 获取铁推荐量
     * @param age
     * @param sex
     * @return
     */
    public static double getIronDRI(Sex sex, int age) {
        double r;
        boolean f = sex.equals(MALE);
        if (age >= 80) {
            r = 12;
        } else if (age >= 65) {
            r = 12;
        } else if (age >= 50) {
            r = 12;
        } else if (age >= 18) {
            r = f ? 12 : 20;
        } else if (age >= 14) {
            r = f ? 16 : 18;
        } else if (age >= 11) {
            r = f ? 15 : 18;
        } else if (age >= 7) {
            r = 13;
        } else if (age >= 4) {
            r = 10;
        } else if (age >= 1) {
            r = 9;
        } else if (age >= 0.5) {
            r = 10;
        } else {
            r = 0.3;
        }
        return r;
    }

    /**
     * 获取锌推荐量
     * @param age
     * @param sex
     * @return
     */
    public static double getZincDRI(Sex sex, int age) {
        double r;
        boolean f = sex.equals(MALE);
        if (age >= 80) {
            r = f ? 12.5 : 7.5;
        } else if (age >= 65) {
            r = f ? 12.5 : 7.5;
        } else if (age >= 50) {
            r = f ? 12.5 : 7.5;
        } else if (age >= 18) {
            r = f ? 12.5 : 7.5;
        } else if (age >= 14) {
            r = f ? 11.5 : 8.5;
        } else if (age >= 11) {
            r = f ? 10 : 9;
        } else if (age >= 7) {
            r = 7.0;
        } else if (age >= 4) {
            r = 5.5;
        } else if (age >= 1) {
            r = 4.0;
        } else if (age >= 0.5) {
            r = 3.5;
        } else {
            r = 2.0;
        }
        return r;
    }

    /**
     * 获取硒推荐量
     * @param age
     * @return
     */
    public static double getSeleniumDRI(int age) {
        double r;
        if (age >= 80) {
            r = 60;
        } else if (age >= 65) {
            r = 60;
        } else if (age >= 50) {
            r = 60;
        } else if (age >= 18) {
            r = 60;
        } else if (age >= 14) {
            r = 60;
        } else if (age >= 11) {
            r = 55;
        } else if (age >= 7) {
            r = 40;
        } else if (age >= 4) {
            r = 30;
        } else if (age >= 1) {
            r = 25;
        } else if (age >= 0.5) {
            r = 20;
        } else {
            r = 15;
        }
        return r;
    }

    /**
     * 获取铜推荐量
     * @param age
     * @return
     */
    public static double getCopperDRI(int age) {
        double r;
        if (age >= 80) {
            r = 0.8;
        } else if (age >= 65) {
            r = 0.8;
        } else if (age >= 50) {
            r = 0.8;
        } else if (age >= 18) {
            r = 0.8;
        } else if (age >= 14) {
            r = 0.8;
        } else if (age >= 11) {
            r = 0.7;
        } else if (age >= 7) {
            r = 0.5;
        } else if (age >= 4) {
            r = 0.4;
        } else if (age >= 1) {
            r = 0.3;
        } else if (age >= 0.5) {
            r = 0.3;
        } else {
            r = 0.3;
        }
        return r;
    }

    /**
     * 获取猛推荐量
     * @param age
     * @return
     */
    public static double getManganeseDRI(int age) {
        double r;
        if (age >= 80) {
            r = 4.5;
        } else if (age >= 65) {
            r = 4.5;
        } else if (age >= 50) {
            r = 4.5;
        } else if (age >= 18) {
            r = 4.5;
        } else if (age >= 14) {
            r = 4.5;
        } else if (age >= 11) {
            r = 4.0;
        } else if (age >= 7) {
            r = 3.0;
        } else if (age >= 4) {
            r = 2.0;
        } else if (age >= 1) {
            r = 1.5;
        } else if (age >= 0.5) {
            r = 0.7;
        } else {
            r = 0.01;
        }
        return r;
    }

    /**
     * 获取维生素C推荐量
     * @param age
     * @return
     */
    public static double getVitaminCDRI(int age) {
        double r;
        if (age >= 80) {
            r = 100;
        } else if (age >= 65) {
            r = 100;
        } else if (age >= 50) {
            r = 100;
        } else if (age >= 18) {
            r = 100;
        } else if (age >= 14) {
            r = 100;
        } else if (age >= 11) {
            r = 90;
        } else if (age >= 7) {
            r = 65;
        } else if (age >= 4) {
            r = 50;
        } else if (age >= 1) {
            r = 40;
        } else if (age >= 0.5) {
            r = 40;
        } else {
            r = 40;
        }
        return r;
    }

    /**
     * 获取水推荐量（单位：升）
     * @param age
     * @return
     */
    public static double getWaterDRI(Sex sex, int age) {
        double r;
        boolean f = sex.equals(MALE);
        if (age >= 80) {
            r = f ? 1.7 : 1.5;
        } else if (age >= 65) {
            r = f ? 1.7 : 1.5;
        } else if (age >= 50) {
            r = f ? 1.7 : 1.5;
        } else if (age >= 18) {
            r = f ? 1.7 : 1.5;
        } else if (age >= 14) {
            r = f ? 1.4 : 1.2;
        } else if (age >= 11) {
            r = f ? 1.3 : 1.1;
        } else if (age >= 7) {
            r = 1.0;
        } else if (age >= 4) {
            r = 0.8;
        } else if (age >= 1) {
            r = 0;
        } else if (age >= 0.5) {
            r = 0;
        } else {
            r = 0;
        }
        return r;
    }
}

