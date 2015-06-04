package com.sp.lib.widget.nav;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.sp.lib.widget.nav.title.ITab;

import java.util.ArrayList;

public class TabBar extends LinearLayout {
    ArrayList<TabItem> tabs = new ArrayList<>();

    public TabBar(Context context) {
        this(context, null);
    }

    public TabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addTab(TabItem tab) {
        tabs.add(tab);
    }
}
