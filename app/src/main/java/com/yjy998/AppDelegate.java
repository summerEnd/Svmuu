package com.yjy998;

import android.content.Context;

import com.sp.lib.SApplication;
import com.yjy998.common.entity.User;
import com.yjy998.common.Constant;
import com.yjy998.common.test.TestActivity;

/**
 * 程序入口,存放全局数据，进行初始化等操作
 */
public class AppDelegate extends SApplication {

    private static AppDelegate instance;
    private User mSharedUser;
    private boolean isLogined;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //测试入口
        setMainTest(TestActivity.class);
        //是否调试模式
        setDebug(BuildConfig.DEBUG);
    }

    /**
     * 获取app实例
     */
    public static AppDelegate getInstance() {
        return instance;
    }

    public void logout() {
        setLogined(false);
        setUser(null);
        //清除登录参数
        getSharedPreferences(Constant.PRE_LOGIN, Context.MODE_PRIVATE)
                .edit()
                .putString(Constant.PRE_LOGIN_PASSWORD, null)
                        //.putString(Constant.PRE_LOGIN_PHONE, null) //先不清理登录账号
                .putString(Constant.PRE_LOGIN_PASSWORD_RSA, null)
                .commit();
    }

    public User getUser() {
        if (mSharedUser == null) {
            mSharedUser = new User();
        }
        return mSharedUser;
    }

    public void setUser(User user) {
        mSharedUser = user;
    }

    public boolean isUserLogin() {
        return isLogined;
    }

    public void setLogined(boolean isLogined) {
        this.isLogined = isLogined;
    }
}
