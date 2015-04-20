package com.yjy998.ui.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yjy998.R;

public class TabItem extends LinearLayout implements Checkable, View.OnClickListener {
    ImageView imageView;
    TextView textView;
    Drawable mDrawable;
    ColorStateList mTextColor;
    boolean mChecked;
    CheckListener checkListener;
    final int STATUS_CHECKED[] = new int[]{android.R.attr.state_checked};
    final int STATUS_UNCHECKED[] = new int[]{-android.R.attr.state_checked};

    public TabItem(Context context) {
        this(context, null);
    }

    public TabItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        imageView = new ImageView(context, attrs);
        textView = new TextView(context, attrs);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        textView.setLayoutParams(params);
        addView(imageView);
        addView(textView);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabItem);
        mChecked = a.getBoolean(R.styleable.TabItem_checked, false);
        mDrawable = a.getDrawable(R.styleable.TabItem_tabDrawable);
        mTextColor = a.getColorStateList(R.styleable.TabItem_tabTextColor);
        a.recycle();
        setChecked(mChecked);
        super.setOnClickListener(this);
    }

    @Override
    public void setChecked(boolean checked) {

        int color;
        if (checked) {
            mDrawable.setState(STATUS_CHECKED);
            color = mTextColor.getColorForState(STATUS_CHECKED, 0);
        } else {
            mDrawable.setState(STATUS_UNCHECKED);
            color = mTextColor.getColorForState(STATUS_UNCHECKED, 0);
        }
        Drawable currentDrawable = mDrawable.getCurrent();
        imageView.setImageDrawable(currentDrawable);
        textView.setTextColor(color);

        if (checkListener != null && checked) {
            checkListener.onSelected(this);
        }

        this.mChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        if (isChecked()) {
            setChecked(false);
        } else {
            setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (!isChecked()) {
            setChecked(true);
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        throw new RuntimeException("setOnClickListener is not supported");
    }

    public interface CheckListener {
        public void onSelected(TabItem view);
    }

    public CheckListener getCheckListener() {
        return checkListener;
    }

    public void setCheckListener(CheckListener checkListener) {
        this.checkListener = checkListener;
    }
}
