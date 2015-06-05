package com.yjy998.ui.view.number;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.util.Log;
import android.widget.TextView;

import com.sp.lib.common.util.SLog;
import com.yjy998.common.util.NumberUtil;

public class MoneyGrow extends GrowNumber {
    private TextView textView;
    double max;

    public MoneyGrow(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void setValue(float number) {

        textView.setText("￥" + NumberUtil.format(number*max));
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void start() {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "Value", 0, 1);
        animator.setDuration(1300);

        ObjectAnimator scale = ObjectAnimator.ofPropertyValuesHolder(textView, PropertyValuesHolder.ofFloat("ScaleX", 1, 1.4f, 1), PropertyValuesHolder.ofFloat("ScaleY", 1, 1.4f, 1));
        scale.setDuration(500);
        set.playSequentially(animator, scale);
        set.start();
    }
}
