package com.sp.lib.widget.material;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MaterialBackWave extends View {

    //波纹半径
    float radius = 0;
    //波纹圆心
    float cx, cy;
    Paint mPaint = new Paint();
    ObjectAnimator animator;
    ObjectAnimator cancelAnimator;
    int dur = 1200;

    public MaterialBackWave(Context context) {
        this(context, null);
    }

    public MaterialBackWave(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialBackWave(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        animator = ObjectAnimator.ofFloat(this, "Value", 0, 1);
        animator.setDuration(dur);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (radius <= 0) {
            return;
        }

        canvas.drawCircle(cx, cy, radius, mPaint);
    }

    public void start(float cx, float cy) {
        this.cx = cx;
        this.cy = cy;
        animator.start();
    }

    public void cancel() {

        float cur = animator.getCurrentPlayTime() / (float) dur;
        if (cur<1){
            cancelAnimator = ObjectAnimator.ofFloat(this, "Value", cur, 1);
            cancelAnimator.setDuration(200);
            cancelAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    clearValue();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            cancelAnimator.start();
        }else{
            clearValue();
        }
        animator.cancel();
    }

    void clearValue(){
        this.cx = 0;
        this.cy = 0;
        radius = 0;
        invalidate();
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void setValue(float value) {
        radius=getWidth()*value;
        invalidate();
    }
}
