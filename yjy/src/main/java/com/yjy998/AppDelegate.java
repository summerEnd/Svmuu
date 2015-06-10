package com.yjy998;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.Gravity;

import com.sp.lib.SApplication;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.JsonUtil;
import com.yjy998.common.entity.User;
import com.yjy998.common.Constant;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
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

        ContextUtil.setToastLayout(R.layout.toast_layout, Gravity.NO_GRAVITY);
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

    /**
     * 重新调取接口，刷新用户信息
     */
    public void refreshUserInfo() {
        SRequest request = new SRequest();

        YHttpClient.getInstance().getByMethod("/h5/account/assentinfo", request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                AppDelegate.getInstance().setUser(JsonUtil.get(response.data, User.class));
            }
        });
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
