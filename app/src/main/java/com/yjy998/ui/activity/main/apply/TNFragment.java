package com.yjy998.ui.activity.main.apply;


import android.view.View;

public class TNFragment extends BaseApply implements View.OnClickListener {


    public float getRate(int total) {
        if (total < 80 * W) {
            return 0.4f;
        } else {
            return 0.35f;
        }
    }

    public float getKeep(int total) {
        if (total < 80 * W) {
            return total / 10;
        } else {
            return total / 5;
        }
    }

    public int getFeeDays() {
        return 2;
    }


}
