package com.sp.lib.widget.pager.title;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.sp.lib.R;

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
        setChecked(a.getBoolean(0, false));
        a.recycle();
    }


    @Override
    public void setChecked(boolean b) {
        if (b) {
            setTextColor(Color.BLACK);
        } else {
            setTextColor(Color.GRAY);
        }
        isChecked = b;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }


}
