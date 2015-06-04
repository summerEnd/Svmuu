package com.sp.lib.widget.nav.title;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.sp.lib.R;

import java.util.LinkedList;
import java.util.List;

public class PageStrip extends LinearLayout implements ViewPager.OnPageChangeListener {
    /**
     * the height of the indicator
     */
    private int indicatorHeight;
    /**
     * the drawable used as indicator
     */
    private Drawable indicatorDrawable;

    /**
     * show the indicator or not
     */
    private boolean showIndicator;

    private ViewPager mPager;
    /**
     * all tabs are add in this list
     */
    private List<ITab> tabs = new LinkedList<ITab>();
    /**
     * the tab current selected
     */
    private ITab selectedTab;
    /**
     * the listener of the tab click
     */
    private OnTabClick onTabClick = new OnTabClick();

    OnTitleChangeListener mOnTitleChangeListener;

    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    public PageStrip(Context context) {
        this(context, null);
    }

    public PageStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ITab);
        indicatorDrawable = a.getDrawable(R.styleable.ITab_indicator);
        if (indicatorDrawable == null) {
            indicatorDrawable = new ColorDrawable(a.getColor(R.styleable.ITab_indicator, Color.BLUE));
        }
        indicatorHeight = a.getInt(R.styleable.ITab_indicateHeight, 3);
        showIndicator = a.getBoolean(R.styleable.ITab_showIndicator, true);
        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child instanceof ITab) {
                ITab tab = (ITab) child;
                addTabInner(tab);
            }
        }
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        super.dispatchDraw(canvas);
        indicatorDrawable.draw(canvas);
    }

    /**
     * <p>
     * set a ViewPager to control.In this method a {@link android.support.v4.view.ViewPager.OnPageChangeListener OnPageChangeListener}
     * will be set for this ViewPager.so,don't call the {@link android.support.v4.view.ViewPager#setOnPageChangeListener(android.support.v4.view.ViewPager.OnPageChangeListener) ViewPager.setOnPageChangeListener}
     * for this ViewPager .call {@link com.sp.lib.widget.nav.title.PageStrip#setPageChangeListener(android.support.v4.view.ViewPager.OnPageChangeListener) PageStrip.setOnPageChangeListener}
     * instead </p>
     *
     * @param pager the page to control
     */
    public void setViewPager(ViewPager pager) {
        mPager = pager;
        mPager.addOnPageChangeListener(this);
    }

    public void setPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    private void addTabInner(ITab tab) {
        tabs.add(tab);
        if (tab instanceof View) {
            ((View) tab).setOnClickListener(onTabClick);
        }
        if (tab.isTabSelected()) {
            selectedTab = tab;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }


        View childAt = tabs.get(position).getView();
        int drawableWidth = childAt.getWidth();

        int leftOffset = childAt.getLeft();
        if (position < tabs.size() - 1) {
            View nextTab = tabs.get(position + 1).getView();
            leftOffset += (nextTab.getLeft() - childAt.getLeft()) * positionOffset;
            drawableWidth += (nextTab.getWidth() - childAt.getWidth()) * positionOffset;
        }

        int bottom = childAt.getBottom();

        indicatorDrawable.setBounds(leftOffset, bottom - indicatorHeight, drawableWidth + leftOffset, bottom);
        adjustTabPosition();
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }
        check(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    /**
     * check the tab at this position
     *
     * @param position the position of the tab
     */
    public void check(int position) {
        ITab tab = tabs.get(position);
        if (selectedTab != null && tab == selectedTab) {
            return;
        }
        selectedTab = tab;
        for (ITab mTab : tabs) {
            mTab.setTabSelect(tab == mTab);
        }
        if (mPager != null) {
            mPager.setCurrentItem(position);
        }
    }

    private class OnTabClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            //noinspection SuspiciousMethodCalls
            check(tabs.indexOf(v));
        }
    }

    /**
     * adjust the tab position;
     */
    void adjustTabPosition() {
        if (getParent() instanceof HorizontalScrollView) {
            HorizontalScrollView scrollView = (HorizontalScrollView) getParent();
            Rect indicator = indicatorDrawable.getBounds();

            int scrollX = scrollView.getScrollX();
            int leftOffset = indicator.left - scrollX;
            int rightOffset = indicator.right - scrollX - scrollView.getWidth();
            if (leftOffset < 0) {
                scrollView.scrollBy(leftOffset, 0);
            } else if (rightOffset > 0) {
                scrollView.scrollBy(rightOffset, 0);
            }
        }
    }

    public int getTabCount() {
        return tabs.size();
    }

    public interface OnTitleChangeListener {
        void onSelected(int position, ITab tab);

        void onUnSelected(int position, ITab tab);
    }
}
