package com.yjy998.ui.activity.main.apply;


public class T9Fragment extends BaseApply {

    @Override
    public float getRate(int total) {
        return 0.35f;
    }

    @Override
    public float getKeep(int total) {
        return total / 5;
    }

    @Override
    public int getFeeDays() {
        return 10;
    }

    @Override
    public String getType() {
        return "T9";
    }

    @Override
    public String getPro_id() {
        return "3";
    }
}
