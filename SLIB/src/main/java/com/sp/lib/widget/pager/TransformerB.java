package com.sp.lib.widget.pager;

import android.support.v4.view.ViewPager;
import android.view.View;

public class TransformerB implements ViewPager.PageTransformer {

    float degree = 90;

    @Override
    public void transformPage(View view, float position) {
        view.setPivotY(view.getHeight()/2);

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 1) { // [-1,1]

            float factor = Math.abs(position);
            float rotation = degree * position;

            if (position > 0) {
                //右边
                view.setPivotX(0);
                view.setRotationY(rotation);

            } else {
                //左边
                view.setPivotX(view.getWidth());
                view.setRotationY(rotation);
            }
            view.setAlpha(1);
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}
