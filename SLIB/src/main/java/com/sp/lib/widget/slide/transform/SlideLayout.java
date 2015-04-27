package com.sp.lib.widget.slide.transform;

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
     * 滑动的View
     */
    View mSlideView;
    /**
     * 菜单
     */
    View mMenuView;

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
    int TOUCHE_SLOP = 90;

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

            //拖动距离太小，返回
            if (Math.abs(mDragOffset) < TOUCHE_SLOP) {
                mDragHelper.abort();
                if (isOpen()) {
                    open();
                } else {
                    close();
                }
                return;
            }

            if (mDragOffset >= 0) {
                //向右滑动
                open();
            } else {
                //向左滑动
                close();
            }
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
            dispatchTransform(newLeft);
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
            dispatchTransform(mSlideView.getLeft());
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    void dispatchTransform(float left) {

        if (transformer != null) {
            float rate = left / (float) MAX_WIDTH;
            transformer.transform(mSlideView, mMenuView, Math.abs(rate));
        }

        //设置菜单不可见，不然它也会相应触屏事件
        int menuVisibility = mMenuView.getVisibility();
        if (left == 0 && menuVisibility != INVISIBLE) {
            mMenuView.setVisibility(INVISIBLE);
        } else if (left != 0 && menuVisibility != VISIBLE) {
            mMenuView.setVisibility(VISIBLE);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mSlideView = getChildAt(getChildCount() - 1);
        mMenuView = getChildAt(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (super.onTouchEvent(event)) {
            return true;
        }
        mDragHelper.processTouchEvent(event);
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            lastToucheX = mSlideView.getLeft();
            mDragOffset = 0;
        } else {
            mDragOffset = mSlideView.getLeft() - lastToucheX;
        }
        return true;
    }

    /**
     * @return the max width
     */
    public int getMaxWidth() {
        return MAX_WIDTH;
    }

    /**
     * @return true the pane is open,false otherwise
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * open the pane
     */
    public void open() {
        mDragHelper.smoothSlideViewTo(mSlideView, MAX_WIDTH, mSlideView.getTop());
        invalidate();
        isOpen = true;
    }

    /**
     * close the pane
     */
    public void close() {
        mDragHelper.smoothSlideViewTo(mSlideView, 0, mSlideView.getTop());
        invalidate();
        isOpen = false;
    }

    public void toggle() {
        if (isOpen()) {
            close();
        } else {
            open();
        }
    }
}
