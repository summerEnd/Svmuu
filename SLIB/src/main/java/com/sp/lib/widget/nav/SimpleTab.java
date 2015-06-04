package com.sp.lib.widget.nav;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.sp.lib.widget.nav.title.ITab;

public class SimpleTab extends View implements ITab {

    Drawable mDrawable;
    String text;
    Paint mPaint;
    int drawablePadding;
    int textHeight;
    boolean selected;

    public SimpleTab(Context context) {
        this(context, null);
    }

    public SimpleTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int[] attrArray = new int[]{android.R.attr.src, android.R.attr.checked, android.R.attr.text, android.R.attr.textSize, android.R.attr.textColor, android.R.attr.drawablePadding};
        mPaint = new Paint();
        TypedArray a = context.obtainStyledAttributes(attrs, attrArray);
        mDrawable = a.getDrawable(0);
        selected = a.getBoolean(1, false);
        text = a.getString(2);
        mPaint.setTextSize(a.getDimensionPixelSize(3, 20));
        mPaint.setColor(a.getColor(4, Color.BLACK));
        drawablePadding = a.getDimensionPixelSize(5, 10);

        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        a.recycle();

        if (mDrawable == null) {
            mDrawable = new ColorDrawable();
        }

        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
        setTabSelect(selected);
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        Rect rect = new Rect();
        mPaint.getTextBounds("H", 0, 1, rect);
        textHeight = rect.height();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        Rect bounds = mDrawable.getBounds();

        if (widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST) {
            width = getPaddingLeft() + getPaddingRight() + (int) Math.max(bounds.width(), mPaint.measureText(text));
        }

        if (heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST) {
            height = getPaddingTop() + getPaddingBottom() + bounds.height() + drawablePadding + textHeight;
        }

        setMeasuredDimension(width, height);


        int drawableTop = getPaddingTop();
        int drawableLeft = (getMeasuredWidth() - bounds.width()) / 2;
        mDrawable.setBounds(drawableLeft, drawableTop, drawableLeft + bounds.width(), drawableTop + bounds.height());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        mDrawable.getCurrent().draw(canvas);
        canvas.drawText(text, getWidth() / 2, mDrawable.getBounds().bottom + drawablePadding + textHeight / 2, mPaint);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setTabSelect(boolean selected) {
        this.selected = selected;
        if (selected) {
            mDrawable.setState(new int[]{android.R.attr.state_checked});
        } else {
            mDrawable.setState(new int[]{-android.R.attr.state_checked});
        }
        invalidate();
    }

    @Override
    public boolean isTabSelected() {
        return selected;
    }
}
