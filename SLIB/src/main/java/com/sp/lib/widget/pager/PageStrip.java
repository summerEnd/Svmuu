package com.sp.lib.widget.pager;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.LinkedList;
import java.util.List;

public abstract class PageStrip extends HorizontalScrollView {
    private int indicatorHeight;
    private Drawable indicatorDrawable;
    private int showIndicator;

    private ViewPager mPager;
    private List<PagerTab> tabs = new LinkedList<PagerTab>();
    private PagerTab curTab;
    private OnTabClick onTabClick = new OnTabClick();

    public PageStrip(Context context) {
        this(context, null);
    }

    public PageStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child instanceof PagerTab) {
                PagerTab tab = (PagerTab) child;
                tabs.add(tab);
                tab.setOnClickListener(onTabClick);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void setViewPager(ViewPager pager) {

        ViewParent parent = pager.getParent();

        if (parent != null) {
            ((ViewGroup) parent).removeView(pager);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void addTab(int index, PagerTab tabView) {
        addView(tabView, index);
        tabs.add(tabView);
    }

    public void addTab(PagerTab tabView) {
        addView(tabView);
        tabs.add(tabView);
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

    public abstract class PagerTab extends FrameLayout {

        public PagerTab(Context context) {
            super(context);
        }

        public PagerTab(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public PagerTab(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public abstract void onSelected();

        public abstract void onUnSelected();
    }
}
