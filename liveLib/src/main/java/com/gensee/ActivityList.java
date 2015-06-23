package com.gensee;

import android.app.Activity;

import com.gensee.routine.Routine;
import com.gensee.rtrdemo.MainActivity;
import com.sp.lib.activity.DEBUGActivity;
import com.sp.lib.activity.STestActivity;

import java.util.List;

public class ActivityList extends STestActivity{
    @Override
    protected void addTest(List<Class<? extends Activity>> activities) {
        activities.add(MainActivity.class);
        activities.add(DEBUGActivity.class);
        Routine routine=new Routine();
    }
}
