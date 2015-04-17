package com.sp.lib.widget.chart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

public class ChartLine {

    private List<ChartPoint> points = new ArrayList<ChartPoint>();
    Paint paint = new Paint();
    Path path = new Path();
    RectF bound;

    public ChartLine() {
        paint.setColor(Color.RED);
    }

    public void add(ChartPoint point) {

        if (points.size() == 0) {
            bound = new RectF(point.xValue, point.yValue, point.xValue, point.yValue);
        } else {
            if (point.xValue <= bound.left) {
                bound.left = point.xValue;
            }

            if (point.xValue >= bound.right) {
                bound.right = point.xValue;
            }

            if (point.yValue >= bound.top) {
                bound.top = point.yValue;
            }

            if (point.yValue <= bound.bottom) {
                bound.bottom = point.yValue;
            }
        }

        points.add(point);
    }

    public void draw(Canvas canvas, float width, float height) {
        if (points.size() == 0) {
            return;
        }
        float xMax = bound.width();
        float yMax = bound.height();
        path.reset();
        canvas.save();
        canvas.scale(xMax / width, yMax / height);
        path.moveTo(points.get(0).xValue, points.get(0).yValue);
        for (ChartPoint point : points) {
            float xValue = point.xValue;
            float yValue = point.yValue;
            canvas.drawPoint(xValue, yValue, paint);
            path.moveTo(xValue, yValue);
        }
        canvas.drawPath(path, paint);
        canvas.restore();

    }
}
