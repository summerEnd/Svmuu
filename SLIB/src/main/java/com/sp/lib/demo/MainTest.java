package com.sp.lib.demo;

import android.app.Activity;
import android.content.Intent;

public class MainTest extends SlibDemoWrapper {
    Class<? extends Activity> cls;

    protected MainTest(SlibDemo demo, Class<? extends Activity> cls) {
        super(demo, "Enter app", "this is the activity set by SlibDemo.main");
        this.cls = cls;
    }


    @Override
    protected void onCreate() {
        startActivity(new Intent(getActivity(), cls));
    }

}
