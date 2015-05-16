package com.yjy998.ui.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sp.lib.widget.LayoutSquare;
import com.yjy998.R;

public class CircleItem extends FrameLayout {
    TextView boldText;
    TextView normalText;
    public static final int CIRCLE_BLUE = 0;
    public static final int CIRCLE_RED = 1;
    int layoutSquare;
    View child;

    public CircleItem(Context context) {
        this(context, null);
    }

    public CircleItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        child = inflate(context, R.layout.circle_item, null);
        boldText = (TextView) child.findViewById(R.id.boldText);
        normalText = (TextView) child.findViewById(R.id.normalText);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleItem);
        boldText.setText(array.getString(R.styleable.CircleItem_boldText));
        normalText.setText(array.getString(R.styleable.CircleItem_normalText));

        int circle = array.getInt(R.styleable.CircleItem_circle, CIRCLE_BLUE);
        switch (circle) {
            case CIRCLE_BLUE: {
                child.setBackgroundResource(R.drawable.blue_circle);
                boldText.setTextColor(getResources().getColor(R.color.deepBlue));
                normalText.setTextColor(getResources().getColor(R.color.deepBlue));
                break;
            }
            case CIRCLE_RED: {
                child.setBackgroundResource(R.drawable.red_circle);
                boldText.setTextColor(getResources().getColor(R.color.red));
                normalText.setTextColor(getResources().getColor(R.color.red));
                break;
            }
        }

        array.recycle();
        array = context.obtainStyledAttributes(attrs, new int[]{R.styleable.LockView_layoutSquare});
        layoutSquare = array.getInt(R.styleable.LockView_layoutSquare, LayoutSquare.WIDTH);
        array.recycle();
        addView(child);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int newSpec = LayoutSquare.apply(layoutSquare, widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(newSpec, newSpec);

    }

    public void setBoldText(String text) {
        boldText.setText(text);
    }

    public void setNormalText(String text) {
        normalText.setText(text);
    }

    public void setCircle(int circle) {
        switch (circle) {
            case CIRCLE_BLUE: {
                child.setBackgroundResource(R.drawable.blue_circle);
                boldText.setTextColor(getResources().getColor(R.color.deepBlue));
                normalText.setTextColor(getResources().getColor(R.color.deepBlue));
                break;
            }
            case CIRCLE_RED: {
                child.setBackgroundResource(R.drawable.red_circle);
                boldText.setTextColor(getResources().getColor(R.color.red));
                normalText.setTextColor(getResources().getColor(R.color.red));
                break;
            }
        }
    }
}
