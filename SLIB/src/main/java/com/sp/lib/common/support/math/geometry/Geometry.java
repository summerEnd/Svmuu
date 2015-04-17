package com.sp.lib.common.support.math.geometry;

import android.graphics.PointF;

public class Geometry {
    /**
     * 获取两直线交点
     * @return null代表相交
     */
    public static PointF getCross(Line l1, Line l2) {
        //无法构成直线
        if (!l1.canMakeLine() || !l2.canMakeLine()) {
            return null;
        }
        float a1 = l1.getA();
        float a2 = l2.getA();
        //直线平行
        if (a1 == a2) {
            return null;
        }

        float b1 =l1.getB();
        float b2 = l2.getB();
        PointF crossPoint=new PointF();
        crossPoint.x = (b2 - b1) / (a1 - a2);
        crossPoint.y = a1 * crossPoint.x + b1;

        return crossPoint;

    }
}
