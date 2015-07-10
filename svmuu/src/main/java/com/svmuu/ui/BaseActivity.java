package com.svmuu.ui;

import android.annotation.TargetApi;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.svmuu.R;
import com.svmuu.common.receiver.UserChangeReceiver;

/**
 * 所有Activity的基类
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.windowTitleBackground));
        }
        registerReceiver(mChangeReceiver, new IntentFilter(UserChangeReceiver.ACTION_USER_CHANGED));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mChangeReceiver);
    }

    private UserChangeReceiver mChangeReceiver = new UserChangeReceiver() {
        @Override
        public void onChanged() {
            onUserChanged();
        }
    };

    public void onUserChanged() {

    }
}
