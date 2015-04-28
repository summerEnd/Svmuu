package com.sp.lib.widget.pager;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import java.util.LinkedList;
import java.util.List;

public class PageStrip extends HorizontalScrollView implements ViewPager.OnPageChangeListener {
    private int indicatorHeight;
    private Drawable indicatorDrawable;
    private int showIndicator;

    private ViewPager mPager;
    private List<PagerTab> tabs = new LinkedList<PagerTab>();
    private PagerTab curTab;
    private OnTabClick onTabClick = new OnTabClick();
    private float leftOffset;

    public PageStrip(Context context) {
        this(context, null);
    }

    public PageStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        indicatorDrawable = new ColorDrawable(Color.BLUE);
        indicatorDrawable.setBounds(1, 1, 1, 1);

    }

    @Override
    protected void onFinishInflate() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child instanceof PagerTab) {
                PagerTab tab = (PagerTab) child;
                addTabInner(tab);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        indicatorDrawable.draw(canvas);
    }


    public void setViewPager(ViewPager pager) {
        mPager = pager;
        mPager.setOnPageChangeListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void addTab(int index, PagerTab tabView) {
        addView(tabView, index);
        addTabInner(tabView);
    }

    public void addTab(PagerTab tabView) {
        addView(tabView);
        addTabInner(tabView);
    }

    private void addTabInner(PagerTab tab) {
        tabs.add(tab);
        tab.setOnClickListener(onTabClick);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        View childAt = getChildAt(position);
        int leftOffset = childAt.getLeft();
        indicatorDrawable.setBounds(leftOffset, 10, childAt.getWidth() + leftOffset, 20);
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class OnTabClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            PagerTab clickedTab = (PagerTab) v;
            if (curTab != null && clickedTab == curTab) {
                return;
            }
            if (curTab != null) {
                if (curTab == clickedTab) {
                    return;
                } else {
                    curTab.onUnSelected();
                }
            }
            clickedTab.onSelected();
            curTab = clickedTab;
        }
    }

}
