package com.sp.lib.widget.nav.title;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public abstract class SPagerTab extends View implements ITab {

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
    public View getView() {
        return mTab;
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
    public final void setTabSelect(boolean b) {
        if (isChecked == b) {
            return;
        }
        onTabSelected(b);
        isChecked = b;

    }

    protected void onTabSelected(boolean selected) {
    }

    @Override
    public boolean isTabSelected() {
        return isChecked;
    }

    protected abstract View onCreateView();
}
