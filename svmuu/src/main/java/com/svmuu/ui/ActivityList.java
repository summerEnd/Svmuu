package com.svmuu.ui;

import android.app.Activity;

import com.sp.lib.activity.DEBUGActivity;
import com.sp.lib.activity.STestActivity;
import com.svmuu.ui.activity.A;
import com.svmuu.ui.activity.EnterActivity;
import com.svmuu.ui.activity.MyCircleActivity;
import com.svmuu.ui.activity.MainActivity;
import com.svmuu.ui.activity.SearchActivity;
import com.svmuu.ui.activity.live.LiveActivity;

import java.util.List;

public class ActivityList extends STestActivity{
    @Override
    protected void addTest(List<Class<? extends Activity>> activities) {
        activities.add(A.class);
        activities.add(EnterActivity.class);
        activities.add(MainActivity.class);
        activities.add(MyCircleActivity.class);
        activities.add(LiveActivity.class);
        activities.add(SearchActivity.class);
        activities.add(DEBUGActivity.class);
    }
}
