package com.svmuu.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.svmuu.R;

/**
 * 主要是为了应对搜索页面的新样式
 */
public class CustomSearchViewEx extends CustomSearchView {
    public CustomSearchViewEx(Context context) {
        super(context);
    }

    public CustomSearchViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSearchViewEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View onCreateView(Context context) {
        View inflate = View.inflate(context, R.layout.search_view_ex, this);
        findViewById(R.id.searchButton).setOnClickListener(searchListener);
        return inflate;
    }
}
