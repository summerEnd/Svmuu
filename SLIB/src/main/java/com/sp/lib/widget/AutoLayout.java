package com.sp.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自动换行布局
 */
public class AutoLayout extends ViewGroup {

    public AutoLayout(Context context) {
        super(context);
    }

    public AutoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean change, int l, int t, int r, int b) {
        int count = getChildCount();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();

        int rightBound = getWidth() - paddingRight;
        int childStartX = paddingLeft;
        int childStartY = paddingTop;
        int rowHeight = 0;

        for (int index = 0; index < count; index++) {
            View child = getChildAt(index);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if (childStartX + childWidth + params.leftMargin + params.rightMargin > rightBound) {
                childStartX = params.leftMargin;
                childStartY += rowHeight;
                rowHeight = 0;
            } else {
                rowHeight = Math.max(rowHeight, params.bottomMargin + params.topMargin + childHeight);
            }
            int left = childStartX + params.leftMargin;
            int top = childStartY + params.topMargin;
            child.layout(left, top, left + childWidth, top + childHeight);

            childStartX = childStartX + childWidth + params.leftMargin + params.rightMargin;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            throw new IllegalArgumentException("width must be Exactly");
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            int count = getChildCount();
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = getPaddingTop() + getPaddingBottom();
            int rowHeight = 0;
            int rightBound = width - getPaddingRight();
            int rowLength = getPaddingLeft();
            int takeWidthSpace;
            int takeHeightSpace;
            for (int index = 0; index < count; index++) {
                View child = getChildAt(index);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                child.measure(child.getMeasuredWidth(), child.getMeasuredHeight());
                takeWidthSpace = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                takeHeightSpace = child.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;
                if (rowLength + takeWidthSpace > rightBound) {
                    height += rowHeight;
                    rowHeight = takeHeightSpace;
                    rowLength = getPaddingLeft()+takeWidthSpace;
                } else {
                    rowLength += takeWidthSpace;
                    rowHeight = Math.max(rowHeight, takeHeightSpace);

                }
            }
            height += rowHeight;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public static class LayoutParams extends MarginLayoutParams {
        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }
}
