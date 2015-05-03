package com.sp.lib.widget.pager.title;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class TextTable extends TextView implements IPagerTab {
    private boolean isChecked;

    public TextTable(Context context) {
        this(context, null);
    }

    public TextTable(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextTable(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = getContext().obtainStyledAttributes(attrs, new int[]{android.R.attr.checked});
        setSelected(a.getBoolean(0, false));
        a.recycle();
    }

    @Override
    public View getView() {
        return this;
    }

    protected void onTabSelected(boolean selected) {
        if (selected) {
            setTextColor(Color.BLACK);
        } else {
            setTextColor(Color.GRAY);
        }

    }


    @Override
    public final void setTabSelect(boolean selected) {
        onTabSelected(selected);
        isChecked = selected;
    }

    @Override
    public boolean isTabSelected() {
        return isChecked;
    }

}
