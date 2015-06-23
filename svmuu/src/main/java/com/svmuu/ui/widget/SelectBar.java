package com.svmuu.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.sp.lib.common.image.drawable.CircleLRDrawable;
import com.svmuu.R;


public class SelectBar extends LinearLayout {
    final int COLOR_CHECKED;
    final int COLOR_NORMAL;
    CircleLRDrawable leftDrawable;
    CircleLRDrawable rightDrawable;
    CircleLRDrawable totalDrawable;
    OnSelectListener listener;
    int checkedChildIndex = 0;

    public SelectBar(Context context) {
        this(context, null);
    }

    public SelectBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SelectBar);

        COLOR_CHECKED = a.getColor(R.styleable.SelectBar_selectColor, 0);
        COLOR_NORMAL = a.getColor(R.styleable.SelectBar_normalColor, 0);

        leftDrawable = new CircleLRDrawable(COLOR_CHECKED);
        leftDrawable.setMode(CircleLRDrawable.Mode.MODE_LEFT);
        rightDrawable = new CircleLRDrawable(COLOR_NORMAL);
        rightDrawable.setMode(CircleLRDrawable.Mode.MODE_RIGHT);

        totalDrawable = new CircleLRDrawable(0);
        totalDrawable.setDrawBord(true);
        totalDrawable.setBorderColor(COLOR_NORMAL);
        totalDrawable.setMode(CircleLRDrawable.Mode.MODE_NORMAL);
        a.recycle();
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        leftDrawable.draw(canvas);

        canvas.save();
        canvas.translate(leftDrawable.getBounds().width(), 0);
        rightDrawable.draw(canvas);
        canvas.restore();

        totalDrawable.draw(canvas);

        canvas.restore();
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();

        int drawableWidth = (width - getPaddingLeft() - getPaddingRight()) / 2;
        int drawableHeight = height - getPaddingBottom() - getPaddingTop();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = left + drawableWidth;
        int bottom = top + drawableHeight;


        leftDrawable.setBounds(left, top, right, bottom);
        rightDrawable.setBounds(left, top, right, bottom);
        totalDrawable.setBounds(left, top, width - getPaddingRight(), height - getPaddingBottom());
    }

    @Override
    protected void onFinishInflate() {
        int count = getChildCount();
        if (count != 2) {
            throw new RuntimeException("must be 2 children");
        }

        getChildAt(0).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedChildIndex = 0;
                onSelectChanged();
            }
        });
        getChildAt(1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedChildIndex = 1;
                onSelectChanged();
            }
        });
        invalidate();
    }

    void onSelectChanged() {
        if (checkedChildIndex == 0) {
            leftDrawable.setSolidColor(COLOR_CHECKED);
            rightDrawable.setSolidColor(COLOR_NORMAL);
        } else {
            leftDrawable.setSolidColor(COLOR_NORMAL);
            rightDrawable.setSolidColor(COLOR_CHECKED);
        }
        if (listener != null) {
            listener.onSelect(checkedChildIndex);
        }
        invalidate();
    }

    public void setListener(OnSelectListener listener) {
        this.listener = listener;
    }

    public interface OnSelectListener {
        void onSelect(int index);
    }
}
