package com.slib.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.sp.lib.activity.DEBUGActivity;
import com.sp.lib.activity.STestActivity;
import com.sp.lib.activity.SlibActivity;

import java.util.List;

public class MainActivity extends STestActivity {

    @Override
    protected void addTest(List<Class<? extends Activity>> activities) {
        activities.add(PagerTitle.class);
        activities.add(HttpTest.class);
        activities.add(DEBUGActivity.class);
        activities.add(AnimTest.class);
        activities.add(AutoLayoutTest.class);
    }
}
