package com.sp.lib.widget.nav;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.sp.lib.widget.nav.title.ITab;

import java.util.ArrayList;

public class TabBar extends LinearLayout implements View.OnClickListener {
    ArrayList<SimpleTab> tabs = new ArrayList<>();
    OnTabSelectListener mOnTabSelectListener;
    private SimpleTab previousTab;

    public TabBar(Context context) {
        this(context, null);
    }

    public TabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);


    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child instanceof SimpleTab) {
                addTab((SimpleTab) child);
            }
        }
    }

    public void addTab(SimpleTab tab) {
        tabs.add(tab);
        if (tab.isTabSelected()) {
            previousTab = tab;
        }
        tab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SimpleTab simpleTab = (SimpleTab) v;
        for (SimpleTab tab : tabs) {
            tab.setTabSelect(false);
        }
        simpleTab.setTabSelect(true);

        if (mOnTabSelectListener != null) {
            mOnTabSelectListener.onSelect(simpleTab);
        }

    }

    public void setOnTabSelectListener(OnTabSelectListener onTabSelectListener) {
        this.mOnTabSelectListener = onTabSelectListener;
    }

    public interface OnTabSelectListener {
        void onSelect(SimpleTab tab);
    }
}
