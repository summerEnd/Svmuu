package com.sp.lib.widget.slide.toggle;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.sp.lib.widget.slide.toggle.shape.ArcShape;
import com.sp.lib.widget.slide.toggle.shape.ArcTransformer;

public class ArcToggle extends ToggleView {
    ArcTransformer transformer;

    public ArcToggle(Context context) {
        this(context, null);
    }

    public ArcToggle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcToggle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        transformer = new ArcTransformer(
                new ArcShape(0, 0, new RectF(), true),
                new ArcShape(0, 360, new RectF(), true)
        );

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int l = getPaddingLeft();
        int r = getPaddingRight();
        int t = getPaddingTop();
        int b = getPaddingBottom();
        transformer.getShapeStart().rectF.set(l, t, width - r, height - b);
        transformer.getShapeEnd().rectF.set(l, t, width - r, height - b);
    }

    public void setUseCenter(boolean useCenter) {
        transformer.outPut(0).useCenter = useCenter;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        transformer.outPut(ratio).draw(canvas, mPaint);
    }
}
