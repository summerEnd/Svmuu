package com.svmuu.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sp.lib.common.image.drawable.CircleLRDrawable;
import com.sp.lib.common.util.ViewUtil;
import com.svmuu.R;


public class SelectBar extends RadioGroup {


    public SelectBar(Context context) {
        super(context);
    }

    public SelectBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        int childCount = getChildCount();
        if (childCount < 2) {
            throw new RuntimeException("must at least has 2 children");
        }

        final CircleLRDrawable firstBg = new CircleLRDrawable(Color.WHITE);
        firstBg.setMode(CircleLRDrawable.Mode.MODE_NORMAL);

        ViewUtil.setBackground(getChildAt(0), firstBg);
        ViewUtil.setBackground(getChildAt(1), firstBg);

        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int count = group.getChildCount();
                for (int i = 0; i < count; i++) {
                    View childAt = group.getChildAt(i);
                    CircleLRDrawable background = (CircleLRDrawable) childAt.getBackground();
                    background.setDrawBord(false);
                    background.setSolidColor(Color.WHITE);
                }
                ((CircleLRDrawable) findViewById(checkedId).getBackground()).setSolidColor(Color.RED);
                invalidate();

            }
        });
    }
}
