package com.svmuu;

import android.os.Build;
import android.text.TextUtils;

import com.sp.lib.SApplication;
import com.svmuu.common.entity.User;

public class AppDelegate extends SApplication{
    private User user;
    private static AppDelegate instance;
    public static AppDelegate getInstance(){return instance;}


    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;

        setDebug(BuildConfig.DEBUG);
    }

    public boolean isLogin(){
        return !TextUtils.isEmpty(getUser().name);
    }

    public User getUser() {
        if (user==null){
            user=new User();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
