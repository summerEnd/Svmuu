package com.yjy998.ui.activity.admin;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.sp.lib.widget.lock.LockView;
import com.sp.lib.widget.lock.NineLock;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.common.Constant;

public class LockActivity extends Activity {
    private String secret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        setContentView(R.layout.activity_lock);
        LockView lockView = (LockView) findViewById(R.id.lockView);

        secret = getSharedPreferences(Constant.PRE_GESTURE, MODE_PRIVATE).getString(Constant.PRE_GESTURE_PASSWORD, null);
        lockView.setLock(new NineLock() {
            @Override
            public boolean tryUnLock() {


                boolean equals = getDrawSecret().equals(secret);
                if (equals) {
                    AppDelegate.getInstance().setEnterBackground(false);
                    finish();
                }
                return equals;
            }
        });
    }

    public static boolean isLockEnabled() {
        SharedPreferences sp = AppDelegate.getInstance().getSharedPreferences(Constant.PRE_GESTURE, MODE_PRIVATE);
        String secret = sp.getString(Constant.PRE_GESTURE_PASSWORD, null);
        boolean enabled = sp.getBoolean(Constant.PRE_GESTURE_ENABLED, false);
        return (enabled && !TextUtils.isEmpty(secret));
    }

    @Override
    public void onBackPressed() {
        //什么也不做，只是为了拦截返回键
    }
}
