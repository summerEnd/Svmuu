package com.sp.lib.widget.pager.title;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.sp.lib.R;

import java.util.LinkedList;
import java.util.List;

public class PageStrip extends LinearLayout implements ViewPager.OnPageChangeListener {
    private int indicatorHeight;
    private Drawable indicatorDrawable;
    private boolean showIndicator;

    private ViewPager mPager;
    private List<IPagerTab> tabs = new LinkedList<IPagerTab>();
    private IPagerTab curTab;
    private OnTabClick onTabClick = new OnTabClick();

    public PageStrip(Context context) {
        this(context, null);
    }

    public PageStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.IPagerTab);
        indicatorDrawable = a.getDrawable(0);
        indicatorHeight = a.getInt(0, 3);
        showIndicator = a.getBoolean(0, true);
        a.recycle();
        if (indicatorDrawable == null) {
            indicatorDrawable = new ColorDrawable(Color.BLUE);
        }
        setOrientation(LinearLayout.HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child instanceof IPagerTab) {
                IPagerTab tab = (IPagerTab) child;
                addTabInner(tab);
            }
        }
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        super.dispatchDraw(canvas);
        indicatorDrawable.draw(canvas);
    }

    public void setViewPager(ViewPager pager) {
        mPager = pager;
        mPager.setOnPageChangeListener(this);
    }

    private void addTabInner(IPagerTab tab) {
        tabs.add(tab);
        if (tab instanceof View) {
            ((View) tab).setOnClickListener(onTabClick);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        View childAt = getChildAt(position);
        View nextChild = getChildAt(position + 1);
        int leftOffset = childAt.getLeft();

        if (nextChild != null) {
            leftOffset += nextChild.getWidth() * positionOffset;
        }

        int bottom = childAt.getBottom();
        indicatorDrawable.setBounds(leftOffset, bottom - indicatorHeight, childAt.getWidth() + leftOffset, bottom);
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        check(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void check(int position) {
        IPagerTab clickedTab = tabs.get(position);
        if (curTab != null && clickedTab == curTab) {
            return;
        }
        if (curTab != null) {
            curTab.setChecked(false);
        }
        clickedTab.setChecked(true);
        curTab = clickedTab;
        if (mPager != null) {
            mPager.setCurrentItem(position);
        }
    }

    private class OnTabClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            check(tabs.indexOf(v));
        }
    }

}
