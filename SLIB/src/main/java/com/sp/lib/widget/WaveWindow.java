package com.sp.lib.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

public class WaveWindow extends PopupWindow {

    private final View contentView;

    public WaveWindow(Context context) {
        super(context);
        contentView = new Button(context);
        setContentView(contentView);
        setWidth(300);
        setHeight(300);
    }

    public void show(View view) {
        showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
    }

    public class WaveView extends View {
        float radius;
        float MAX_RADIUS = 300;
        Paint paint = new Paint();
        ObjectAnimator animator;

        public WaveView(Context context) {
            super(context);
            paint.setColor(0xFFffffff);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            animator = ObjectAnimator.ofFloat(this, "Rate", 0, 100f);
            animator.setDuration(2000);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startWave();
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    animator.cancel();
                    break;
            }

            return super.onTouchEvent(event);
        }

        public void startWave() {

            animator.start();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
        }

        public void setRate(float rate) {
            radius = MAX_RADIUS * rate * 100;
            invalidate();
        }
    }
}
