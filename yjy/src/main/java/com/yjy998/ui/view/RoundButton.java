package com.yjy998.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;

import com.yjy998.R;

public class RoundButton extends TextView {
    private int pressed_color;
    private int normal_color;
    private int radius;
    Paint mPaint = new Paint();
    Path boundPath = new Path();

    public RoundButton(Context context) {
        this(context, null);
    }

    public RoundButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundButton);
        pressed_color = a.getColor(R.styleable.RoundButton_pressedColor, 0);
        normal_color = a.getColor(R.styleable.RoundButton_normalColor, 0);
        mPaint.setColor(normal_color);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        a.recycle();
        setClickable(true);
        setGravity(Gravity.CENTER);
    }

    public void setColor(int color) {
        normal_color = color;
        pressed_color = color-Color.argb(20,0,0,0);
        mPaint.setColor(color);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        buildPath(getMeasuredWidth(), getMeasuredHeight());
    }

    void buildPath(int width, int height) {
        radius = height / 2;

        boundPath.moveTo(radius, 0);
        boundPath.lineTo(width - radius, 0);
        RectF rightRect = new RectF(width - radius * 2, 0, width, height);
        RectF leftRect = new RectF(0, 0, radius * 2, height);
        boundPath.arcTo(rightRect, -90, 180);
        boundPath.lineTo(radius, height);
        boundPath.arcTo(leftRect, 90, 180);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(boundPath, mPaint);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isClickable())
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPaint.setColor(pressed_color);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mPaint.setColor(normal_color);
                    break;
            }
        invalidate();
        return super.onTouchEvent(event);
    }
}
