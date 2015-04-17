package com.yjy998.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.view.MotionEvent;
import android.view.View;

public abstract class TimeLineCover extends View {
    boolean drawLine = false;
    float touchX;
    Paint paint;

    public TimeLineCover(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        PathEffect effects = new DashPathEffect(new float[]{1, 1.5f}, 1);
        paint.setPathEffect(effects);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawLine = true;
                onSelect(touchX);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                drawLine = false;
                break;
        }
        invalidate();
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawLine)
            canvas.drawLine(touchX, 0, touchX, getHeight(), paint);
    }

    protected abstract void onSelect(float touchPoint);
}
