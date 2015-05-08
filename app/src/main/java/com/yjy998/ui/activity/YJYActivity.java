package com.yjy998.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sp.lib.activity.SlibActivity;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.http.Response;
import com.yjy998.http.YHttpClient;
import com.yjy998.http.YHttpHandler;
import com.yjy998.ui.activity.other.LockActivity;
import com.yjy998.ui.pop.YAlertDialogTwoButton;

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
        if (AppDelegate.getInstance().shouldLockScreen() && LockActivity.isLockEnabled()) {
            startActivity(new Intent(this, LockActivity.class));
        }
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 退出登录
     */
    protected void logout() {
        YHttpClient.getInstance().logout(new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                AppDelegate.getInstance().logout();
                Intent intent = new Intent(YJYActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
