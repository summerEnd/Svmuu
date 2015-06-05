package com.slib.demo.material;

import android.app.Activity;

import com.sp.lib.activity.STestActivity;

import java.util.List;

public class MaterialTest extends STestActivity {
    @Override
    protected void addTest(List<Class<? extends Activity>> activities) {
        activities.add(MaterialGridView.class);
        activities.add(MaterialBaseTest.class);
    }
}
