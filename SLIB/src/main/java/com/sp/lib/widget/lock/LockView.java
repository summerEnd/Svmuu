package com.sp.lib.widget.lock;

import android.content.Context;
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

public class LockView extends View {

    private Drawable drawable;

    private int verticalSpacing;
    private int horizontalSpacing;
    private String password;
    private final int[] SELECTED = new int[]{android.R.attr.state_checked};
    private final int[] UNSELECTED = new int[]{-android.R.attr.state_checked};
    NineLock mLock;
    Paint pathPaint;

    float touchX;
    float touchY;

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
        password = a.getString(R.styleable.LockView_password);
        if (TextUtils.isEmpty(password)) {
            password = "";
        }
        mLock = new NineLock(password.toCharArray());

        pathPaint = new Paint();
        pathPaint.setColor(Color.GREEN);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(8);
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
            canvas.save();
            canvas.translate(paddingLeft, getY(row));
            for (int column = 0; column < 3; column++) {
                if (isSelected(row, column)) {
                    drawable.setState(SELECTED);
                } else {
                    drawable.setState(UNSELECTED);
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
        if (action == MotionEvent.ACTION_UP) {
            if (mLock.tryUnLock()) {
                pathPaint.setColor(Color.GREEN);
            } else {
                pathPaint.setColor(Color.RED);
            }
        } else {
            pathPaint.setColor(Color.WHITE);
        }

        invalidate();
        return true;
    }
}
