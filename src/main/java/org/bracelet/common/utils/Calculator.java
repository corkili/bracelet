package org.bracelet.common.utils;

import org.bracelet.common.bp.NeuronNet;
import org.bracelet.common.enums.ActivityType;
import org.bracelet.common.enums.Sex;
import org.bracelet.entity.SleepState;
import org.bracelet.entity.SportState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Calculator {
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

    public static int forecaseCalories(double weight, double height, int age, Sex sex, List<Integer> caloriesList) {
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
                    heightBytes[1], ageBytes[0], ageBytes[1], sexBytes, (byte)(i+1) };
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
                heightBytes[1], ageBytes[0], ageBytes[1], sexBytes, (byte)(input.length + 1) };
        byte[] byte64 = ByteUtils.longToByte64(ByteUtils.byte8ToLong(byte8));
        double[] predictValue = new double[64];
        for (int i = 0; i < predictValue.length; i++) {
            predictValue[i] = byte64[i];
        }
        bp.predict(predictValue);

        return 0;
    }
}
