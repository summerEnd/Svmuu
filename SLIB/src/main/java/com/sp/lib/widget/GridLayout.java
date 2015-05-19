package com.sp.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class GridLayout extends ViewGroup {
    private int mVerticalSpacing = 10;
    private int mHorizontalSpacing = 10;
    private int mColumnNumber = 4;
    private int mItemSize;

    public GridLayout(Context context) {
        super(context);
    }

    public GridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int goneChildren = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == View.GONE) {
                goneChildren++;
            }
            int realIndex = i - goneChildren;

            int row = realIndex / mColumnNumber;
            int column = realIndex % mColumnNumber;
            int left = column * (mItemSize + mVerticalSpacing);
            int top = row * (mItemSize + mHorizontalSpacing);
            child.layout(left, top, left + mItemSize, top + mItemSize);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        mItemSize = (widthSize - mHorizontalSpacing * (mColumnNumber - 1)) / mColumnNumber;
        final int count = getChildCount();

        int goneChildren = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            child.measure(mItemSize, mItemSize);
            if (child.getVisibility() == View.GONE) {
                goneChildren++;
            }
        }
        int visibleCount = count - goneChildren;
        int rowNumber = visibleCount / mColumnNumber + (visibleCount % mColumnNumber == 0 ? 0 : 1);
        int heightSize = rowNumber * mItemSize + (rowNumber + 1) * mVerticalSpacing;
        setMeasuredDimension(widthSize,heightSize);

    }


    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
