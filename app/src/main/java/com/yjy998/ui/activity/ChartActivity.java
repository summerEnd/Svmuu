package com.yjy998.ui.activity;

import android.os.Bundle;

import com.yjy998.R;

public class ChartActivity extends YJYActivity {
    TimeLineFragment fragment = new TimeLineFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        getFragmentManager().beginTransaction().add(R.id.chartContainer, fragment).commit();
    }


}
