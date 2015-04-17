package com.sp.lib.common.support.math.geometry;

import android.graphics.PointF;

/**
 * 线
 */
public class Line {
    PointF pointA;
    PointF pointB;

    public Line(PointF pointA, PointF pointB) {
        this.pointA = pointA;
        this.pointB = pointB;
    }

    /**
     * 是否能确定一条直线
     */
    public boolean canMakeLine() {
        return pointA != null && !pointA.equals(pointB);
    }

    /**
     * 是否垂直x
     *
     * @return
     */
    public boolean isVertical() {
        return pointA.x == pointB.x && pointA.y != pointB.y;
    }

    /**
     * 是否垂直y
     *
     * @return
     */
    public boolean isHorizontal() {
        return pointA.y == pointB.y && pointA.x != pointB.x;
    }

    public float getA() {

        if (isVertical()) {
            return Float.MAX_VALUE;
        } else if (isHorizontal()) {
            return 0;
        }

        return (pointB.y - pointA.y) / (pointB.x - pointA.x);
    }

    public float getB() {

        return pointA.y-getA()*pointA.x;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (o == null || !(o instanceof Line)) {
            return false;
        }

        Line another = (Line) o;

        if (another.isVertical()) {
            if (isVertical()) {
                return another.pointA.x == pointA.x;
            } else {
                return false;
            }
        }

        if (another.getA() != getA()) {
            return false;
        }

        return getB()==another.getB();
    }
}
