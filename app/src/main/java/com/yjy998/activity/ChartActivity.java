package com.yjy998.activity;

import android.os.Bundle;
import android.view.MotionEvent;

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
