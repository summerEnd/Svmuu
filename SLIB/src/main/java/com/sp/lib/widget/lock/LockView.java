package com.sp.lib.widget.lock;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sp.lib.R;
import com.sp.lib.widget.LayoutSquare;

public class LockView extends View {

    private Drawable drawable;
    //垂直间隔
    private int verticalSpacing;
    //水平间隔
    private int horizontalSpacing;

    private final int[] STATUS_SELECTED = new int[]{-android.R.attr.state_first, android.R.attr.state_checked};
    private final int[] STATUS_ERROR = new int[]{-android.R.attr.state_first, -android.R.attr.state_checked};
    private final int[] STATUS_FIRST = new int[]{android.R.attr.state_first};
    //锁
    NineLock mLock;
    //绘制路线的画笔
    Paint pathPaint;
    //没有触点
    private final float NO_TOUCH = -500;
    //当前触摸的X坐标
    float touchX = NO_TOUCH;
    //挡墙触摸的Y坐标
    float touchY = NO_TOUCH;

    private int mLayoutSquare;

    private int lineFailColor;
    private int lineSuccessColor;
    private int lineNormalColor;
    public LockView(Context context) {
        this(context, null);
    }

    public LockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LockView);
        verticalSpacing = a.getDimensionPixelOffset(R.styleable.LockView_verticalSpacing, 0);
        horizontalSpacing = a.getDimensionPixelOffset(R.styleable.LockView_horizontalSpacing, 0);
        drawable = a.getDrawable(R.styleable.LockView_src);
        if (drawable == null) {
            drawable = getResources().getDrawable(R.drawable.tab_selector);
        }
        mLayoutSquare = a.getInt(R.styleable.LockView_layoutSquare, LayoutSquare.NONE);
        String password = a.getString(R.styleable.LockView_password);
        if (TextUtils.isEmpty(password)) {
            password = "";
        }
        mLock = new NineLock(password);
        ColorStateList lineColor = a.getColorStateList(R.styleable.LockView_lineColor);
        lineFailColor = lineColor.getColorForState(new int[]{-android.R.attr.state_first, -android.R.attr.state_checked}, Color.RED);
        lineSuccessColor = lineColor.getColorForState(new int[]{-android.R.attr.state_first, android.R.attr.state_checked}, Color.GREEN);
        lineNormalColor = lineColor.getColorForState(new int[]{android.R.attr.state_first}, Color.WHITE);

        pathPaint = new Paint();
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(8);
        pathPaint.setColor(lineNormalColor);
        a.recycle();
    }

    /**
     * set a reset
     *
     * @param lock
     */
    public void setLock(NineLock lock) {
        lock.setSecret(mLock.getSecret());
        mLock = lock;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mLayoutSquare != LayoutSquare.NONE) {
            widthMeasureSpec = heightMeasureSpec = LayoutSquare.apply(mLayoutSquare, widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int drawableWidth = (getMeasuredWidth() - paddingLeft - paddingRight - horizontalSpacing * 2) / 3;
        int drawableHeight = (getMeasuredHeight() - paddingTop - paddingBottom - verticalSpacing * 2) / 3;
        drawable.setBounds(0, 0, drawableWidth, drawableHeight);
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int paddingLeft = getPaddingLeft();
        int drawableWidth = drawable.getBounds().width();
        int drawableHeight = drawable.getBounds().height();

        for (int row = 0; row < 3; row++) {
            //绘制9个锁
            canvas.save();
            canvas.translate(paddingLeft, getY(row));
            for (int column = 0; column < 3; column++) {
                if (isSelected(row, column)) {
                    drawable.setState(touchX == NO_TOUCH && mLock.tryUnLock()
                            ? STATUS_SELECTED
                            : STATUS_ERROR);
                } else {
                    drawable.setState(STATUS_FIRST);
                }
                drawable.draw(canvas);
                canvas.translate(horizontalSpacing + drawableWidth, 0);
            }
            canvas.restore();
        }
        Path path = new Path();

        String secret = mLock.getSecret();

        if (secret.length() == 0) {
            return;
        }
        int first = mLock.getPosition(secret.charAt(0));
        if (first < 0) {
            return;
        }
        int firstRow = first / 3;
        int firstColumn = first % 3;
        int halfDrawableHeight = drawableHeight / 2;
        int halfDrawableWidth = drawableWidth / 2;

        path.moveTo(getX(firstColumn) + halfDrawableWidth, getY(firstRow) + halfDrawableHeight);
        for (int i = 1; i < secret.length(); i++) {
            int position = mLock.getPosition(secret.charAt(i));
            if (position < 0) {
                break;
            }
            path.lineTo(X(position) + halfDrawableWidth, Y(position) + halfDrawableHeight);
        }
        if (touchX != NO_TOUCH && touchY != NO_TOUCH)
            path.lineTo(touchX, touchY);

        canvas.drawPath(path, pathPaint);
    }


    float X(int position) {
        return getX(position % 3);
    }

    float Y(int position) {
        return getY(position / 3);
    }

    float getX(int column) {
        return getPaddingLeft() + column * (horizontalSpacing + drawable.getBounds().width());
    }

    float getY(int row) {
        return getPaddingTop() + row * (verticalSpacing + drawable.getBounds().height());
    }

    boolean isSelected(int row, int column) {
        return mLock.contains(row, column);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();
        int halfWidth = drawable.getBounds().width() / 2;
        int halfHeight = drawable.getBounds().height() / 2;

        for (int i = 0; i < 9; i++) {
            float distanceX = Math.abs(touchX - X(i) - halfWidth);//触摸点到图片中心的x距离
            float distanceY = Math.abs(touchY - Y(i) - halfHeight);//触摸点到图片中心的y距离
            if (distanceX < halfWidth && distanceY < halfHeight) {
                mLock.appendFixedPosition(i);
            }
        }

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            mLock.reset();
            removeCallbacks(showUnlockResult);
        }

        if (action == MotionEvent.ACTION_UP) {
            if (mLock.tryUnLock()) {
                pathPaint.setColor(lineSuccessColor);
            } else {
                pathPaint.setColor(lineFailColor);
            }
            touchX = NO_TOUCH;
            touchY = NO_TOUCH;
            postDelayed(showUnlockResult, 1000);
        } else {
            pathPaint.setColor(lineNormalColor);
        }
        invalidate();

        return true;
    }

    private Runnable showUnlockResult = new Runnable() {

        @Override
        public void run() {
            mLock.reset();
            invalidate();
        }
    };

    public int getVerticalSpacing() {
        return verticalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
    }

    public int getHorizontalSpacing() {
        return horizontalSpacing;
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
    }

    public int getLineFailColor() {
        return lineFailColor;
    }

    public void setLineFailColor(int lineFailColor) {
        this.lineFailColor = lineFailColor;
    }

    public int getLineSuccessColor() {
        return lineSuccessColor;
    }

    public void setLineSuccessColor(int lineSuccessColor) {
        this.lineSuccessColor = lineSuccessColor;
    }

    public int getLineNormalColor() {
        return lineNormalColor;
    }

    public void setLineNormalColor(int lineNormalColor) {
        this.lineNormalColor = lineNormalColor;
    }
}
