package com.yjy998.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.yjy998.R;

public class RoundButton extends TextView {
    private int pressed_color;
    private int normal_color;
    private int radius;
    Paint mPaint = new Paint();


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
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        radius = getMeasuredHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int leftCircleCenter = radius + getPaddingLeft();
        int rightCircleCenter = getWidth() - getPaddingRight() - radius;

        canvas.drawCircle(leftCircleCenter, radius + getPaddingTop(), radius, mPaint);
        canvas.drawCircle(rightCircleCenter, radius + getPaddingTop(), radius, mPaint);
        canvas.drawRect(leftCircleCenter, getPaddingTop(), rightCircleCenter, getPaddingBottom(), mPaint);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPaint.setColor(pressed_color);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mPaint.setColor(normal_color);
                break;
        }
        return super.onTouchEvent(event);
    }
}
