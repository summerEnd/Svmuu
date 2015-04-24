package com.yjy998.ui.activity.test;

import android.app.Activity;

import com.sp.lib.activity.STestActivity;
import com.yjy998.ui.activity.other.LockActivity;
import com.yjy998.ui.activity.MainActivity;

import java.util.List;

public class TestActivity extends STestActivity {

    @Override
    protected void addTest(List<Class<? extends Activity>> activities) {
        activities.add(MainActivity.class);
        activities.add(LockActivity.class);
        activities.add(ChartActivity.class);
    }


}
