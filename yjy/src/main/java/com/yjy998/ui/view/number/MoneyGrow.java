package com.yjy998.ui.view.number;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void start() {
        com.nineoldandroids.animation.AnimatorSet set=new com.nineoldandroids.animation.AnimatorSet();
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "Value", 0, 1);
        animator.setDuration(1300);

        ObjectAnimator scale = ObjectAnimator.ofPropertyValuesHolder(textView, PropertyValuesHolder.ofFloat("ScaleX", 1, 1.4f, 1), PropertyValuesHolder.ofFloat("ScaleY", 1, 1.4f, 1));
        scale.setDuration(500);

        set.playSequentially(animator, scale);
        set.start();
    }
}
