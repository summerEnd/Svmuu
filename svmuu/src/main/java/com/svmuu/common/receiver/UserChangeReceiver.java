package com.svmuu.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.svmuu.AppDelegate;

/**
 * 用户登录状态改变时调用receiver
 */
public abstract class UserChangeReceiver extends BroadcastReceiver {

    public static final String ACTION_USER_CHANGED = "com.svmuu.common.receiver.UChanged";

    private static boolean login_status = false;//登录状态

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean newStatus = AppDelegate.getInstance().isLogin();
        if (newStatus != login_status) {
            onChanged();
            login_status = newStatus;
        }
    }

    public abstract void onChanged();
}
