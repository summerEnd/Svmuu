package com.sp.lib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class AbsListViewSelector extends View {

    private String sections[]=new String[]{"st","#","A","B","C","D","E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};;

    private int height;

    Paint paint;

    private OnTextSelectedListener mListener;

    int curIndex=-1;

    public AbsListViewSelector(Context context) {
        super(context);
        init();
    }

    public AbsListViewSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AbsListViewSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.DKGRAY);
        paint.setTextAlign(Paint.Align.CENTER);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        paint.setTextSize(dm.scaledDensity * 12);
    }

    public void setOnTextSelectedListener(OnTextSelectedListener mListener) {
        this.mListener = mListener;
    }

    public interface OnTextSelectedListener {
        /**
         * 点击AbsListViewSelector选中的文字发生改变是调用
         *
         * @param text
         */
        public void onTextChange(String text);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float center = getWidth() / 2;
        height = getHeight() / sections.length;
        for (int i = sections.length - 1; i > -1; i--) {
            canvas.drawText(sections[i], center, height * (i + 1), paint);
        }
    }

    private void setHeaderTextAndScroll(float y) {
        int index = (int) (y / height);

        if(index < 0) {
            index = 0;
        }
        if(index > sections.length - 1){
            index = sections.length - 1;
        }
        if (curIndex==index){
            return;
        }
        if (mListener!=null){
            mListener.onTextChange(sections[index]);
            curIndex=index;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {

                setHeaderTextAndScroll(event.getY());
                setBackgroundColor(Color.GREEN);
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                setHeaderTextAndScroll(event.getY());
                return true;
            }
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.TRANSPARENT);
                return true;
            case MotionEvent.ACTION_CANCEL:
                setBackgroundColor(Color.TRANSPARENT);
                return true;
        }
        return super.onTouchEvent(event);
    }
}
