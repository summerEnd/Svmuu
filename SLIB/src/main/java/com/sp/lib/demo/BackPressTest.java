package com.sp.lib.demo;

import android.widget.TextView;

public class BackPressTest extends SlibDemoWrapper {

    private static final String DESCRIPTION = "This is a test for Back Press test";
    int count;
    TextView textView;

    public BackPressTest(SlibDemo demo) {
        super(demo, "Back Press", DESCRIPTION);
    }

    @Override
    public void onCreate() {
        count = 10;
        textView = new TextView(getActivity());
        showPress(count);
        setContentView(textView);
    }

    void showPress(int time) {
        textView.setText(String.format("Will return after %d press", time));
    }

    @Override
    public boolean onBackPressed() {
        count--;
        if (count > 0) {
            showPress(count);
            return true;
        } else {
            return false;
        }
    }
}
