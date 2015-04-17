package com.yjy998;

import android.app.Application;
import android.content.SharedPreferences;

import com.sp.lib.SApplication;
import com.sp.lib.demo.SlibDemo;
import com.sp.lib.support.util.PreferenceUtil;
import com.yjy998.activity.TestActivity;
import com.yjy998.common.preference.AppInfo;

/**
 * 程序入口,存放全局数据，进行初始化等操作
 */
public class AppDelegate extends SApplication {

    private static AppDelegate instance;
    private final int MINUTE = 1000 * 60;
    //锁屏的时间间隔
    private final int BACK_DURATION = 1 * MINUTE;

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

    /**
     * 设置当前app是否进入后台
     */
    public void setEnterBackground(boolean background) {
        PreferenceUtil.getPreference(AppInfo.class).edit()
                .putBoolean(AppInfo.background, background)
                .putLong(AppInfo.enterBackgroundTimeMillis, System.currentTimeMillis())
                .commit();
    }

    /**
     * 是否应锁屏。如果进入后台时间大于{@link com.yjy998.AppDelegate#BACK_DURATION 锁屏间隔}就锁屏
     *
     * @return true 锁屏
     */
    public boolean shouldLockScreen() {
        SharedPreferences appInfo = PreferenceUtil.getPreference(AppInfo.class);
        boolean background = appInfo.getBoolean(AppInfo.background, false);

        if (background) {
            long enterBackgroundTimeMillis = appInfo.getLong(AppInfo.enterBackgroundTimeMillis, 0);
            return System.currentTimeMillis() - enterBackgroundTimeMillis > BACK_DURATION;
        }
        return false;
    }
}
