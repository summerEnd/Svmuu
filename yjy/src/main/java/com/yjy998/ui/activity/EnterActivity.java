package com.yjy998.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.ImageView;

import com.sp.lib.common.support.adapter.GuidePagerAdapter;
import com.sp.lib.common.support.cache.FileObjectCache;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.common.util.ShortCut;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.common.entity.User;
import com.yjy998.common.Constant;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.main.MainActivity;
import com.yjy998.ui.activity.pay.PayTestActivity;

import java.io.File;

/**
 * 这个activity不要继承{@link com.yjy998.ui.activity.base.YJYActivity YJYActivity},因为这个页面不应该锁屏
 */
public class EnterActivity extends Activity {
    ViewPager pager;
    int lastPosition;
    boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppDelegate.getInstance().isFirstStartApplication()) {
            ShortCut.addShortcut(this, getString(R.string.app_name), EnterActivity.class);
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
        //加载引导页图片
        pager.setAdapter(new GuidePagerAdapter(this, new int[]{
                R.drawable.guide1,
                R.drawable.guide2,
                R.drawable.guide3
        }));
        //监听滑动事件
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //如果是最后一页，并且也不能向右滑动，可以接结束
                exit = (positionOffsetPixels == lastPosition) && !pager.canScrollHorizontally(1);
                lastPosition = positionOffsetPixels;
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE && exit) {
                    //滑动结束，根据exit判断是否可以结束
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

        File dir = new File(getCacheDir(), "contract");
        if (!dir.exists()) {
            if (dir.mkdirs())
                new FileObjectCache(dir).clear();//重新进入app清空合约缓存
        }

        //设置下次不再启动引导页
        AppDelegate.getInstance().setIsFirstStartApplication(false);
        //显示loading图片
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setContentView(imageView);
        imageView.setImageResource(R.drawable.loading);

        loginBackground();
    }

    /**
     * 后台自动登录
     */
    void loginBackground() {
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

        YHttpClient.getInstance().getByMethod(this, "/h5/account/assentinfo", request, new YHttpHandler(false) {
            @Override
            protected void onStatusCorrect(Response response) {

                try {
                    AppDelegate.getInstance().setUser(JsonUtil.get(response.data, User.class));
                    AppDelegate.getInstance().setLogined(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    enterMain();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                enterMain();
            }
        });
    }

    private void enterMain() {
        //延迟进入
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(EnterActivity.this, MainActivity.class));
//                startActivity(new Intent(EnterActivity.this, MainActivity.class));

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, 1000);
    }
}
