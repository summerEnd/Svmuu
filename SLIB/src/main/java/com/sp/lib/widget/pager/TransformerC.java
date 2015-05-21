package com.sp.lib.widget.pager;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 开门型
 */
public class TransformerC implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View view, float position) {
        float pageWidth = view.getWidth();
        float MIN_SCALE = 0.2f;
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 1) { // [-1,1]

            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float factor = Math.abs(position);

            if (position > 0) {
                view.setTranslationX(-pageWidth * factor / 3);
                view.setRotationY(-(1 - scaleFactor) * 90);

            } else {
                view.setTranslationX(pageWidth * factor / 3);
                view.setRotationY((1 - scaleFactor) * 90);
            }
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            view.setAlpha(1 - factor);
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}
