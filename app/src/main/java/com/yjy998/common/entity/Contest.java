package com.yjy998.common.entity;

import com.yjy998.R;

import java.io.Serializable;

public class Contest implements Serializable {
    public String profitRatio;
    //区域
    public String area;
    //排名
    public String rank;
    //比赛id
    public String id;
    //比赛名称
    public String name;
    //参赛人数
    public String attenders;
    //比赛类型:1精英赛，2海选赛，3普通赛
    public int type;
    //老师圈号
    public String teachId;
    //开始时间
    public String startTime;
    //结束时间
    public String endTime;

    /**
     * 获取比赛类型
     *
     * @return 资源id，要解析成String
     */
    public int getType() {
        switch (type) {
            case 1: {
                return R.string.Elite;
            }
            case 2: {
                return R.string.sea_race;
            }
            case 3: {
                return R.string.normal_race;
            }
        }
        return R.string.unknow;
    }

    public float getProfitRatio() {
        try {
            return Float.parseFloat(profitRatio);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
