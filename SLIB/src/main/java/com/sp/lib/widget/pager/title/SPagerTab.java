package com.sp.lib.widget.pager.title;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public abstract class SPagerTab extends View implements IPagerTab {

    private View mTab;
    private boolean isChecked;

    public SPagerTab(Context context) {
        this(context, null);
    }

    public SPagerTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SPagerTab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTab = onCreateView();
        if (mTab == null) {
            throw new NullPointerException("must return a view in onCreateView");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mTab.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mTab.draw(canvas);
    }

    @Override
    public void setChecked(boolean b) {
        if (isChecked == b) {
            return;
        }
        if (b) {
            onSelected();
        } else {
            onUnSelected();
        }
        isChecked = b;

    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    public View getTab() {
        return mTab;
    }

    protected abstract void onSelected();

    protected abstract void onUnSelected();

    protected abstract View onCreateView();
}
