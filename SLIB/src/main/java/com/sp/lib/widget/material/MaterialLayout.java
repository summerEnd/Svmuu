package com.sp.lib.widget.material;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.FrameLayout;

public class MaterialLayout extends FrameLayout {
    MaterialBackWave wave;

    public MaterialLayout(Context context) {
        this(context, null);
    }

    public MaterialLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        wave = new MaterialBackWave(context);
        addView(wave);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                wave.start(event.getX(), event.getY());
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                wave.cancel();
                break;
        }

        return true;
    }

    public void setMaterialBackground(int color) {
        wave.setBackgroundColor(color);
    }

    public void setMaterialWave(int color) {
        wave.setColor(color);
    }
}
