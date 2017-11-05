package org.bracelet.common.enums;

public enum ActivityType {
//    Walk_light(240),                       // 步行 - 3.2公里/小时
//    Walk_medium(335.5),                    // 步行 - 4.8公里/小时
//    Walk_heavy(383.3),                     // 步行 - 6.4公里/小时
//    Uphill(575.5),                         // 徒步爬坡 - 5.6公里/小时
//    Running_light(671.1),                  // 慢跑，一般
//    Runnin_medium(766.6),                  // 跑步8公里，1公里/10分钟
//    Aerobice_light(431.1),                 // 健美操，家庭，轻/中度努力
//    Aerobice_medium(575.5),                // 健美操，一般
//    RopeSkipping_medium(985.8),            // 跳绳，适中，一般
//    Baskeball_light(766.6),                // 篮球，游戏
//    Skating_medium(671.1),                 // 滑冰，冰，一般
//    Skiing_medium(766.6),                  // 滑雪，越野，适度的努力
//    Football_medium(958.8),                // 足球，有竞争力
//    Softball_medium(480),                  // 垒球，或快或慢间隔的运动
//    Cycling_light(383.3),                  // 骑自行车，<10英里每小时，休闲
//    Cycling_heavy(766.6),                  // 骑自行车，1213.9英里每小时，适度的努力
//    Cycling_medium(478.8),                 // 骑自行车，固定，一般
//    Swimming_heavy(958.8),                 // 游泳的时候，自由泳，速度快，大力
//    Swimming_medium(766.6),                // 游泳的时候，自由泳，轻/中度努力
//    Tennis_medium(671.1),                  // 网球，一般
//    ClimbStairs(766.6),                    // 散步，爬楼梯
//    WeightLift_light(287.7),               // 举重，轻或中度的器械
//    Cleaning(335.5),                       // 打扫卫生，一般
//    Dancing_medium(575.5),                 // 舞蹈，有氧，芭蕾或现代舞
//    Dancing_light(431.1),                  // 舞蹈，一般
//    Rowing_medium(671.1),                  // 赛艇，平稳的
//    Fencing(575.5),                        // 击剑
//    Fishing(383.3),                        // 钓鱼，一般
//    Golf(383.3),                           // 高尔夫球，一般
//    Handball(1150),                        // 手球，一般
//    Fighting(958.8),                       // 散打，空手道，拳击，跆拳道
//    Rowing_light(383.3);                   // 划船,一般

    Walk_32(134),               // 走路，3.2公里/小时
    Walk_40(174),               // 4.0
    Walk_48(268),               // 4.8
    Walk_56(375),               // 5.6
    Walk_64(536),               // 6.4
    Walk_72(710),               // 7.2
    Walk_80(938),               // 8.0
    Run_60(764),                // 跑步，6.0公里/小时
    Run_65(831),                // 6.5
    Run_70(898),                // 7.0
    Run_75(951),                // 7.5
    Run_80(1018),               // 8.0
    Run_84(1072),               // 8.4
    Run_97(1206),               // 9.7
    Run_108(1340),              // 10.8
    Run_113(1407),              // 11.3
    Run_121(1541),              // 12.1
    Run_129(1675),              // 12.9
    Run_138(1742),              // 13.8
    Run_145(1876),              // 14.5
    Run_160(2010),              // 16.0
    Run_175(2278),              // 17.5
    Run_192(2412),              // 19.2
    Run_208(2519),              // 20.8
    Run_224(2948),              // 22.4
    WalkAndRun(670),            // 走跑结合（跑步不超过10分钟）
    Sleep_Pre(174),             // 准备睡觉
    Sleep(-13),                 // 睡觉
    sitOnly(40),                // 静坐
    sitForWork(67),             // 坐着工作（办公室工作）
    TypeWrite(107),             // 写作、文字、打字（坐）
    StandOnly(134),             // 站着
    StandForWork(268),          // 站着工作
    Driving(134),               // 开车
    Learning(107);              // 上课，做笔记，课堂讨论（坐）

    private double kcal = 0;

    ActivityType(double kcal) {
        this.kcal = kcal;
    }

    public double getKcal() {
        return this.kcal;
    }
}
