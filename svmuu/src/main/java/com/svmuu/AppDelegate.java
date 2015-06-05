package com.svmuu;

import com.sp.lib.SApplication;

public class AppDelegate extends SApplication{
    private static AppDelegate instance;
    public static AppDelegate getInstance(){return instance;}

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }
}
