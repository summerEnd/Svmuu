package com.yjy998.ui.activity.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.ImageView;

import com.sp.lib.common.support.adapter.GuidePagerAdapter;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.common.util.SLog;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.admin.User;
import com.yjy998.common.Constant;
import com.yjy998.http.Response;
import com.yjy998.http.YHttpClient;
import com.yjy998.http.YHttpHandler;
import com.yjy998.ui.activity.MainActivity;
import com.yjy998.ui.activity.YJYActivity;
import com.yjy998.ui.pop.YAlertDialog;

/**
 * 这个activity不要继承{@link com.yjy998.ui.activity.YJYActivity YJYActivity},因为这个页面不应该锁屏
 */
public class EnterActivity extends Activity {
    ViewPager pager;
    int lastPosition;
    boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        if (AppDelegate.getInstance().isFirstStartApplication()) {
            startGuide();
        } else {
            startLoading();
        }
    }

    /**
     * 启动引导页
     */
    void startGuide() {
        pager = new ViewPager(this);
        pager.setAdapter(new GuidePagerAdapter(this, new int[]{
                R.drawable.guide_01,
                R.drawable.guide_02,
                R.drawable.guide_03
        }));
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                exit = (positionOffsetPixels == lastPosition) && !pager.canScrollHorizontally(1);
                lastPosition = positionOffsetPixels;
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE && exit) {

                    startLoading();
                }
            }
        });
        setContentView(pager);
    }

    /**
     * 启动加载页
     */

    void startLoading() {
        AppDelegate.getInstance().setIsFirstStartApplication(false);

        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setContentView(imageView);
        imageView.setImageResource(R.drawable.loading);

        login();
    }

    /**
     * 自动登录
     */
    void login() {
        SharedPreferences sp = getSharedPreferences(Constant.PRE_LOGIN, MODE_PRIVATE);
        String phone = sp.getString(Constant.PRE_LOGIN_PHONE, null);
        String password = sp.getString(Constant.PRE_LOGIN_PASSWORD, null);
        String rsa = sp.getString(Constant.PRE_LOGIN_PASSWORD_RSA, null);

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password) || TextUtils.isEmpty(rsa)) {
            enterMain();
            return;
        }

        SRequest request = new SRequest();
        request.put("login_name", phone);
        request.put("login_passwd", password);
        request.put("login_rsapwd", rsa);

        //登录
        YHttpClient.getInstance().login(this, request, new YHttpHandler(false) {
            @Override
            protected void onStatusFailed(Response response) {
                super.onStatusFailed(response);
                enterMain();
            }

            @Override
            protected void onStatusCorrect(Response response) {
                getUserInfo();
            }
        });
    }

    void getUserInfo() {
        SRequest request = new SRequest();

        YHttpClient.getInstance().getByMethod(this, "/h5/account/assentinfo", request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                AppDelegate.getInstance().setUser(JsonUtil.get(response.data, User.class));
                AppDelegate.getInstance().setLogined(true);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                enterMain();
            }
        });
    }

    private void enterMain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(EnterActivity.this, MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, 1000);
    }
}
