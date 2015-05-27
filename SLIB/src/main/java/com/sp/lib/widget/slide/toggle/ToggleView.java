package com.sp.lib.widget.slide.toggle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ToggleView extends View implements ToggleRatio{
    float ratio = 0;
    Paint mPaint = new Paint();

    public ToggleView(Context context) {
        this(context, null);
    }

    public ToggleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(4f);
        mPaint.setAntiAlias(true);
    }


    public void setRatio(float ratio) {
        this.ratio = ratio;
        invalidate();
    }

    public Paint getPaint() {
        return mPaint;
    }

}
