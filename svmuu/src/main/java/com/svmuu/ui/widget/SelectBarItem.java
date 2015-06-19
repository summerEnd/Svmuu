package com.svmuu.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Lincoln on 15/6/18.
 */
public class SelectBarItem extends TextView{
    private int[] CHECK_STATUS=new int[]{android.R.attr.checked};
    public SelectBarItem(Context context) {
        super(context);
    }

    public SelectBarItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectBarItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] status=super.onCreateDrawableState(extraSpace+1);
        if (status!=null){
            mergeDrawableStates(status,CHECK_STATUS);
        }
        return status;
    }
}
