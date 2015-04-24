package com.yjy998.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sp.lib.activity.SlibActivity;
import com.yjy998.AppDelegate;
import com.yjy998.ui.activity.other.LockActivity;

/**
 * 所有Activity的基类
 */
public class YJYActivity extends SlibActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!AppDelegate.getInstance().didAppRunForeground()) {
            AppDelegate.getInstance().setEnterBackground(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppDelegate.getInstance().shouldLockScreen()) {
            startActivity(new Intent(this, LockActivity.class));
        }
    }

    @Override
    public void onClick(View v) {

    }
}
