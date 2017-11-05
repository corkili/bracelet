package org.bracelet.common.utils;

import org.bracelet.common.enums.ActivityType;
import org.bracelet.common.enums.Sex;

public class Calculator {
    public static final Sex MALE = Sex.MALE;
    public static final Sex FEMALE = Sex.FEMALE;

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
    public static long calCalories(double weight, int minutes, ActivityType activityType) {
        return Math.round(((minutes / 60) * (weight / 100)) * activityType.getKcal());
    }

}
