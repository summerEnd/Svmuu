package com.sp.lib.demo;

import android.content.Intent;

import com.sp.lib.activity.DEBUGActivity;

/**
 * Created by user1 on 2015/4/15.
 */
public class DebugTest extends SlibDemoWrapper{
    protected DebugTest(SlibDemo demo) {
        super(demo,"debug","this activity shows crash information");
    }

    @Override
    protected void onCreate() {
        startActivity(new Intent(getActivity(), DEBUGActivity.class));
    }
}
