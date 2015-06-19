package com.svmuu.ui;

import android.app.Activity;

import com.sp.lib.activity.DEBUGActivity;
import com.sp.lib.activity.STestActivity;
import com.svmuu.ui.activity.LiveListActivity;
import com.svmuu.ui.activity.MainActivity;

import java.util.List;

public class ActivityList extends STestActivity{
    @Override
    protected void addTest(List<Class<? extends Activity>> activities) {
        activities.add(MainActivity.class);
        activities.add(LiveListActivity.class);
        activities.add(DEBUGActivity.class);
    }
}
