package com.sp.lib.widget.slide;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.sp.lib.widget.slide.transform.Transformer;

public class SlideLayout extends FrameLayout {
    ViewDragHelper mDragHelper;
    /**
     * 上一次ACTION_DOWN的x坐标
     */
    int lastToucheX = 0;

    /**
     * 拖动的距离
     */
    int mDragOffset;

    /**
     * 0到{@link SlideLayout#MAX_WIDTH}
     */
    float slideWidth;

    /**
     * 滑动的View
     */
    View mSlideView;

    /**
     * 打开的最大宽度
     *
     * @param context
     */
    int MAX_WIDTH = 600;

    /**
     * 触发打开或关闭的最小滑动距离
     *
     * @param context
     */
    int EFFECT_WIDTH = 90;

    Transformer transformer;

    private boolean isOpen;

    public SlideLayout(Context context) {
        this(context, null);
    }

    public SlideLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDragHelper = ViewDragHelper.create(this, 1f, new ViewDragCallback());
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            boolean canSlide = (child == mSlideView);
            return canSlide;
        }

        @Override
        public void onViewDragStateChanged(int state) {

        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {

            if (mDragOffset >= 0) {
                //向右滑动
                if (mDragOffset > EFFECT_WIDTH) {
                    open();
                } else {
                    close();
                }
            } else {
                //向左滑动
                if (Math.abs(mDragOffset) > EFFECT_WIDTH) {
                    close();
                } else {
                    open();
                }
            }
            invalidate();
        }

        @Override
        public int getOrderedChildIndex(int index) {
            return super.getOrderedChildIndex(index);
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            final int leftBound = getPaddingLeft();
            final int rightBound = MAX_WIDTH;
            final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
            slideWidth = newLeft;
            float rate = slideWidth / (float) MAX_WIDTH;
            dispatchTransform(rate);

            return newLeft;
        }
    }

    public Transformer getTransformer() {
        return transformer;
    }

    public void setTransformer(Transformer transformer) {
        this.transformer = transformer;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            float rate = mSlideView.getLeft() / (float) MAX_WIDTH;
            dispatchTransform(rate);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    void dispatchTransform(float rate) {
        if (transformer != null) {
            transformer.transform(getChildAt(1), getChildAt(0), Math.abs(rate));
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mSlideView = getChildAt(getChildCount() - 1);
        if (mSlideView != null) {
            slideWidth = mSlideView.getLeft();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            lastToucheX = mSlideView.getLeft();
        } else {
            mDragOffset = mSlideView.getLeft() - lastToucheX;
        }
        return true;
    }

    public int getMaxWidth() {
        return MAX_WIDTH;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {

        mDragHelper.smoothSlideViewTo(mSlideView, MAX_WIDTH, mSlideView.getTop());
        invalidate();
        isOpen=true;
    }

    public void close() {

        mDragHelper.smoothSlideViewTo(mSlideView, 0, mSlideView.getTop());
        invalidate();
        isOpen=false;

    }
}
