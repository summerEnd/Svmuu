package com.yjy998.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yjy998.R;

public class TwoTextItem extends LinearLayout {
    private TextView textViewPrefix;
    private TextView textViewValue;

    private String textName;

    public TwoTextItem(Context context) {
        this(context, null);
    }

    public TwoTextItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwoTextItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(HORIZONTAL);

        textViewPrefix = new TextView(context);
        textViewValue = new TextView(context, attrs);

        addView(textViewPrefix);
        addView(textViewValue);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TwoTextItem);

        int prefixColor = array.getColor(R.styleable.TwoTextItem_prefixTextColor, getResources().getColor(R.color.textColorGray));
        float prefixSize = array.getDimensionPixelSize(R.styleable.TwoTextItem_prefixTextSize, 22);
        textName = array.getString(R.styleable.TwoTextItem_prefix);
        textViewPrefix.setText(textName);
        textViewPrefix.setTextColor(prefixColor);
        textViewPrefix.getPaint().setTextSize(prefixSize);
        array.recycle();
    }

    public void setText(String text) {
        textViewValue.setText(text);
    }

    public void setText(int resId) {
        textViewValue.setText(resId);
    }
}
