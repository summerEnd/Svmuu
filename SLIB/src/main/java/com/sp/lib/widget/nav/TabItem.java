package com.sp.lib.widget.nav;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.sp.lib.R;
import com.sp.lib.widget.nav.title.ITab;

public class TabItem extends FrameLayout implements ITab {
    private Drawable mDrawable;
    private int resId;
    CharSequence title;
    private boolean selected = false;
    private View mView;

    public TabItem(Context context) {
        this(context, null);
    }

    public TabItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int[] attrsArray = new int[]{android.R.attr.src};
        TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
        mDrawable = array.getDrawable(0);
        if (mDrawable==null){
            mDrawable=new ColorDrawable();
        }
        array.recycle();
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public void setTabSelect(boolean selected) {
        this.selected = selected;
        int state = selected ? android.R.attr.state_checked : -android.R.attr.state_checked;
        mDrawable.setState(new int[]{state});
        mDrawable.getCurrent();
    }

    @Override
    public boolean isTabSelected() {
        return selected;
    }

    public void setCustomView(View view) {
        removeView(mView);
        mView = view;
        addView(view);
    }
}
