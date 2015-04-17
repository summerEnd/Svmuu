package com.yjy998;

import android.app.Application;

import com.sp.lib.Slib;
import com.sp.lib.demo.SlibDemo;
import com.yjy998.activity.MainActivity;
import com.yjy998.activity.TestActivity;

/**
 * 程序入口,存放全局数据，进行初始化等操作
 */
public class AppDelegate extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Slib.initialize(this, BuildConfig.DEBUG);
        SlibDemo.main = TestActivity.class;
    }
}
