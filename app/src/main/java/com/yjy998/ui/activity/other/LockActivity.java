package com.yjy998.ui.activity.other;

import android.os.Bundle;

import com.sp.lib.widget.lock.LockView;
import com.sp.lib.widget.lock.NineLock;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.ui.activity.YJYActivity;

public class LockActivity extends YJYActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_lock);
        LockView lockView = (LockView) findViewById(R.id.lockView);
        lockView.setLock(new NineLock() {
            @Override
            public boolean tryUnLock() {
                boolean equals = "456".equals(getSecret());
                if (equals) {
                    AppDelegate.getInstance().setEnterBackground(false);
                    finish();
                }
                return equals;
            }
        });
    }

    @Override
    public void onBackPressed() {
        //什么也不做，只是为了拦截返回键
    }
}
