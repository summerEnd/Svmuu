package com.sp.lib.widget.slide.toggle.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.shapes.Shape;

public class ArcShape extends Shape {

    public float startAngle;
    public float sweepAngle;
    public RectF rectF;
    public boolean useCenter;
    public ArcShape(float startAngle, float sweepAngle, RectF rectF,boolean useCenter) {
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
        if (rectF == null) {
            rectF = new RectF();
        }
        this.rectF = rectF;
        this.useCenter = useCenter;
    }


    public ArcShape() {
        this(0,0,null,true);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawArc(rectF,startAngle,sweepAngle,useCenter,paint);
    }

}
