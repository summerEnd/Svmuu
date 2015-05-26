package com.sp.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class ToggleView extends View {
    float ratio = 0;
    Paint p = new Paint();
    int lineEndRight;

    public ToggleView(Context context) {
        this(context, null);
    }

    public ToggleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(4f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int mid = getHeight() / 2;
        int topPadding = getPaddingTop();
        int bottomPadding = getPaddingBottom();
        ;
        int leftPadding = getPaddingLeft();
        int rightPadding = getPaddingRight();
        int lineWidth = width - leftPadding - rightPadding;
        float finalPositionRatio = 0.4f;//x轴最终位置与宽度的比例
        float yFloat = 5;//y轴波动
        float xMin = leftPadding+lineWidth*finalPositionRatio;
        float xMax=width-rightPadding;
        float curX=xMax-(xMax-xMin)*ratio;
        canvas.save();
        canvas.rotate(180*(1-ratio),width/2,height/2);
        //画上面的
        canvas.drawLine(
                leftPadding,
                topPadding + (mid - topPadding) * ratio,
                curX,
                topPadding - yFloat * ratio,
                p);
        canvas.drawLine(
                leftPadding,
                mid,
                width - rightPadding,
                mid,
                p);
        canvas.drawLine(
                leftPadding,
                height - bottomPadding - (height - bottomPadding - mid) * ratio,
                curX,
                height - bottomPadding + yFloat * ratio,
                p);

        if (ratio == 1) {
            canvas.drawCircle(leftPadding, mid, 2f, p);
        }
        canvas.restore();
    }


    public void setRatio(float ratio) {
        this.ratio = ratio;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
