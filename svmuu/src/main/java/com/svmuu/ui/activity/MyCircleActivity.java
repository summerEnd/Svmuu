package com.svmuu.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sp.lib.common.util.ContextUtil;
import com.svmuu.R;
import com.svmuu.ui.BaseActivity;
import com.svmuu.ui.activity.live.LiveListFragment;
import com.svmuu.ui.widget.CustomSearchView;
import com.svmuu.ui.widget.SelectBar;

public class MyCircleActivity extends BaseActivity implements SelectBar.OnSelectListener, CustomSearchView.Callback {
    SelectBar selectBar;
    private LiveListFragment fragment;
    CustomSearchView searchView;

    private final String MY_CIRCLE = "/moblieapi/mycircle";
    private final String SEARCH_MY_CIRCLE = "/moblieapi/searchmycircle";
    private final String LIVE = "/moblieapi/live";
    private final String SEARCH_LIVE = "/moblieapi/searchonline";

    private int barIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_list);
        selectBar = (SelectBar) findViewById(R.id.selectedBar);
        selectBar.setListener(this);

        searchView = (CustomSearchView) findViewById(R.id.searchView);
        searchView.setCallback(this);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fragment = new LiveListFragment();
        fragment.setUrlAndKey(LIVE, "*");
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragment).commit();
    }


    @Override
    public void onSelect(int index) {
        barIndex = index;
        ContextUtil.toast(index);
        TextView first = (TextView) selectBar.getChildAt(0);
        TextView second = (TextView) selectBar.getChildAt(1);
        if (index == 0) {
            first.setTextColor(selectBar.getCOLOR_NORMAL());
            second.setTextColor(selectBar.getCOLOR_CHECKED());
            fragment.setUrlAndKey(LIVE, "*");
        } else {
            first.setTextColor(selectBar.getCOLOR_CHECKED());
            second.setTextColor(selectBar.getCOLOR_NORMAL());
            fragment.setUrlAndKey(MY_CIRCLE, "*");
        }
        fragment.requestRefresh();

    }

    @Override
    public void onSearch(String key) {
        if (barIndex == 0) {
            fragment.setUrlAndKey(SEARCH_LIVE, key);
        } else {
            fragment.setUrlAndKey(SEARCH_MY_CIRCLE, key);
        }
        fragment.requestRefresh();
    }

    @Override
    public void onJump() {

    }
}
