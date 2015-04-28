package com.sp.lib.widget.pager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by user1 on 2015/4/28.
 */
public class PagerTab extends FrameLayout{
    View view;

    public PagerTab(Context context) {
        this(context, null);
    }

    public PagerTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerTab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        view = onCreateTabView(getContext());
        addView(view);
    }

    protected View onCreateTabView(Context context) {
        TextView textView = new TextView(context);
        return textView;
    }

    public void onSelected() {
        if (view instanceof TextView) {
            ((TextView) view).setText("onSelected");
        }
    }

    public void onUnSelected() {
        if (view instanceof TextView) {
            ((TextView) view).setText("onUnSelected");
        }
    }
}
