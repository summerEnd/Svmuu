package com.yjy998.ui.activity.test;

import android.os.Bundle;

import com.yjy998.R;
import com.yjy998.ui.activity.other.TimeLineFragment;
import com.yjy998.ui.activity.YJYActivity;

public class ChartActivity extends YJYActivity {
    TimeLineFragment fragment = new TimeLineFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        getSupportFragmentManager().beginTransaction().add(R.id.chartContainer, fragment).commit();
    }


}
