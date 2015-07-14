package com.svmuu.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.widget.ImageView;

import com.sp.lib.common.support.adapter.GuidePagerAdapter;
import com.sp.lib.common.support.cache.FileObjectCache;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.DisplayUtil;
import com.sp.lib.common.util.ShortCut;
import com.svmuu.AppDelegate;
import com.svmuu.R;
import com.svmuu.common.adapter.chat.ChatTagHandler;
import com.svmuu.common.config.Preference;
import com.svmuu.common.config.Preference.USER;
import com.svmuu.common.entity.User;
import com.svmuu.common.http.HttpHandler;
import com.svmuu.common.http.HttpManager;
import com.svmuu.common.http.Response;
import com.svmuu.ui.BaseActivity;
import com.svmuu.ui.pop.LoginActivity;

import org.apache.http.Header;
import org.json.JSONException;

import java.io.File;

public class EnterActivity extends BaseActivity {
    ViewPager pager;
    int lastPosition;
    boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppDelegate.getInstance().isFirstStartApplication()) {
            ShortCut.addShortcut(this, getString(R.string.app_name), EnterActivity.class);
            //暂时没有引导页 startGuide();
            startLoading();
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
                R.drawable.intro_item_manrun_1,
                R.drawable.intro_item_manrun_2,
                R.drawable.intro_item_manrun_1
        }));
        //监听滑动事件
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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

        //设置下次不再启动引导页
        AppDelegate.getInstance().setIsFirstStartApplication(false);
        //显示loading图片
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setContentView(imageView);
        imageView.setImageResource(R.drawable.loading);
        tryLogin();

    }


    private void tryLogin() {
        SharedPreferences sp_user = Preference.get(this, USER.class);

        if (!sp_user.getBoolean(USER.IS_SAVE_PASSWORD, false)) {
            enterMain();
            return;
        }


        final String userName = sp_user.getString(USER.USER_NAME, "");
        final String password = sp_user.getString(USER.USER_PASSWORD, "");

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            enterMain();
            return;
        }

        SRequest request = new SRequest("login");

        request.put("name", userName);
        request.put("pwd", password);


        HttpManager.getInstance().postMobileApi(null, request, new HttpHandler(false) {
            @Override
            public void onResultOk(int statusCOde, Header[] headers, Response response) {


                User user = AppDelegate.getInstance().getUser();
                user.name = userName;
                user.password = password;
                LoginActivity.handleLoginResponse(EnterActivity.this);
                enterMain();
            }

            @Override
            public void onResultError(int statusCOde, Header[] headers, Response response) throws JSONException {
                super.onResultError(statusCOde, headers, response);
                enterMain();
            }

            @Override
            public void onException() {
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
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, 1000);
    }
}
