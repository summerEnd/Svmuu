package com.sp.lib.widget.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public abstract class AbsProgressBar extends View {

    private int maxProgress;
    private int progress;

    public AbsProgressBar(Context context) {
        super(context);
    }

    public AbsProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbsProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onProgressChanged(int maxProgress, int progress) {

    }

    @Override
    protected void onDraw(Canvas canvas) {

    }
}
