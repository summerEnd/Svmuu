package com.slib.demo;

import android.app.Activity;

import com.slib.demo.material.AndroidLTest;
import com.slib.demo.material.MaterialBaseTest;
import com.slib.demo.material.MaterialTest;
import com.sp.lib.activity.DEBUGActivity;
import com.sp.lib.activity.STestActivity;

import java.util.List;

public class MainActivity extends STestActivity {

    @Override
    protected void addTest(List<Class<? extends Activity>> activities) {
        activities.add(AndroidLTest.class);
        activities.add(MaterialTest.class);
        activities.add(PagerTitle.class);
        activities.add(HttpTest.class);
        activities.add(DEBUGActivity.class);
        activities.add(AnimTest.class);
        activities.add(AutoLayoutTest.class);
        activities.add(JiHuanSongTest.class);
        activities.add(WaveTest.class);
        activities.add(DreamTest.class);
        activities.add(ToggleTest.class);
    }
}
