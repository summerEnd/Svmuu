package com.sp.lib.widget.nav.title;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class TextPageTab extends TextView implements ITab {
    private boolean isChecked;

    public TextPageTab(Context context) {
        this(context, null);
    }

    public TextPageTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextPageTab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = getContext().obtainStyledAttributes(attrs, new int[]{android.R.attr.checked});
        setTabSelect(a.getBoolean(0, false));
        a.recycle();
    }

    @Override
    public View getView() {
        return this;
    }

    public void onTabSelected(boolean selected) {
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
