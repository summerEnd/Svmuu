package com.yjy998.ui.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sp.lib.common.util.TextPainUtil;
import com.sp.lib.widget.LayoutSquare;
import com.yjy998.R;

public class CircleItem extends FrameLayout {
    TextView boldText;
    TextView normalText;
    public static final int CIRCLE_BLUE = 0;
    public static final int CIRCLE_RED = 1;
    int layoutSquare;
    View child;
    private boolean isDrawCover=false;private Paint p;
    float textVertical;
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

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (isDrawCover){

            if (p==null){
                p=new Paint();
                p.setTextAlign(Paint.Align.CENTER);
                p.setAntiAlias(true);
                p.setTextSize(getResources().getDimension(R.dimen.text_20px));
                textVertical=getHeight()/2+TextPainUtil.getBaseLineOffset(p);
            }
            p.setColor(Color.argb(130, 0, 0, 0));

            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, p);
            p.setColor(Color.WHITE);

            canvas.drawText("配额已用完",getWidth()/2,textVertical,p);
        }
    }

    public boolean isDrawCover() {
        return isDrawCover;
    }

    public void setIsDrawCover(boolean isDrawCover) {
        this.isDrawCover = isDrawCover;
    }
}
