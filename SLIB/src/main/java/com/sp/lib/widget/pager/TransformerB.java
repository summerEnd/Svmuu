package com.sp.lib.widget.pager;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.sp.lib.common.util.SLog;

/**
 * 直角切换
 */
public class TransformerB implements ViewPager.PageTransformer {

    /**
     * 旋转的角度
     */
    float degree = 90;

    /**
     * @param degree 两张页面的夹角
     */
    public TransformerB(float degree) {
        this.degree = 180 - degree;
    }

    /**
     * 根据页数来自动计算两个页面之间的夹角
     */
    public TransformerB(int page) {
        degree = 360 / page;
    }

    public TransformerB() {
        degree = 90;
    }

    @Override
    public void transformPage(View view, float position) {
        view.setPivotY(view.getHeight() / 2);

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 1) { // [-1,1]

            float rotation = degree * position;
            if (rotation > 90) {
                view.setAlpha(0);
            } else {
                view.setAlpha(1);
            }

            if (position > 0) {
                //右边
                view.setPivotX(0);
                view.setRotationY(rotation);

            } else {
                //左边

                view.setPivotX(view.getWidth());
                view.setRotationY(rotation);
            }

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}
