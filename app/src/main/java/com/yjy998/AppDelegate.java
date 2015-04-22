package com.yjy998;

import com.sp.lib.SApplication;
import com.yjy998.account.User;
import com.yjy998.ui.activity.TestActivity;

/**
 * 程序入口,存放全局数据，进行初始化等操作
 */
public class AppDelegate extends SApplication {

    private static AppDelegate instance;
    private User mSharedUser;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setMainTest(TestActivity.class);
        setDebug(BuildConfig.DEBUG);
        setEnterBackground(false);
    }

    /**
     * 获取app实例
     */
    public static AppDelegate getInstance() {
        return instance;
    }

    public User getUser() {
        if (mSharedUser == null) {
            mSharedUser = new User();
        }
        return mSharedUser;
    }

    public boolean isUserLogin() {
        return getUser().id != User.ID_NO_USER;
    }
}
