package com.sp.lib.widget.nav;

import android.content.Context;
import android.content.res.ColorStateList;
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

import com.sp.lib.R;
import com.sp.lib.widget.nav.title.ITab;

public class SimpleTab extends View implements ITab {

    private Drawable mDrawable;
    private String text;
    private Paint mPaint;
    private int drawablePadding;
    private int textHeight;
    private boolean selected;
    private int color;
    private int selectedColor;

    public SimpleTab(Context context) {
        this(context, null);
    }

    public SimpleTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SimpleTab);

        mDrawable = a.getDrawable(R.styleable.SimpleTab_tabIcon);
        selected = a.getBoolean(R.styleable.SimpleTab_tabChecked, false);
        text = a.getString(R.styleable.SimpleTab_tabText);
        mPaint.setTextSize(a.getDimensionPixelSize(R.styleable.SimpleTab_tabTextSize, 20));
        ColorStateList colorList = a.getColorStateList(R.styleable.SimpleTab_tabTextColors);
        drawablePadding = a.getDimensionPixelSize(R.styleable.SimpleTab_tabIconPadding, 10);
        a.recycle();

        if (colorList != null) {
            color = colorList.getColorForState(new int[]{-android.R.attr.state_checked}, Color.BLACK);
            selectedColor = colorList.getColorForState(new int[]{android.R.attr.state_checked}, Color.BLACK);
        } else {
            color = Color.BLACK;
            selectedColor = Color.BLACK;
        }

        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);

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
            mPaint.setColor(selectedColor);
            mDrawable.setState(new int[]{android.R.attr.state_checked});
        } else {
            mPaint.setColor(color);
            mDrawable.setState(new int[]{-android.R.attr.state_checked});
        }
        invalidate();
    }

    @Override
    public boolean isTabSelected() {
        return selected;
    }
}
