package com.yjy998.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sp.lib.common.support.adapter.EmptyAdapter;

import java.util.List;

public class FloatingTab extends LinearLayout {

    private View topView;
    private View floatView;
    private View bottomView;
    private boolean shouldIntercept = false;

    public FloatingTab(Context context) {
        this(context, null);
    }

    public FloatingTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingTab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


        topView = new TextView(context);
        topView.setBackgroundColor(Color.RED);
        addView(topView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));

        floatView = new TextView(context);
        floatView.setBackgroundColor(Color.GRAY);
        addView(floatView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120));

        ListView bottomView = new ListView(context);
        bottomView.setAdapter(new EmptyAdapter(context, 40));
        this.bottomView = bottomView;
        addView(bottomView);

        if (bottomView instanceof ListView) {
            bottomView.setOnScrollListener(new ListScrollListener());
        }
    }


    private class ListScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            View child = view.getChildAt(firstVisibleItem);
            if (child != null) {
                int listFirstChildTop = child.getTop();


            }
        }
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return super.onInterceptHoverEvent(event);
    }
}
