package com.yjy998.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sp.lib.activity.STestActivity;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends STestActivity {

    @Override
    protected void addTest(List<Class<? extends Activity>> activities) {
        activities.add(MainActivity.class);
        activities.add(LockActivity.class);
        activities.add(ChartActivity.class);
    }


}
