package com.sp.lib.widget.slide.toggle.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;

public class SLine extends Shape {
    public
    float startX, startY, endX, endY;

    public SLine() {
    }

    public SLine(float startX, float startY, float endX, float endY) {
        set(startX, startY, endX, endY);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawLine(startX, startY, endX, endY, paint);
    }

    public void set(float startX, float startY, float endX, float endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }


}
