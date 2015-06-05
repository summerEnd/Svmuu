package com.svmuu.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.ImageView;

import com.sp.lib.activity.SlibActivity;
import com.sp.lib.common.support.adapter.GuidePagerAdapter;
import com.sp.lib.common.support.cache.FileObjectCache;
import com.sp.lib.common.util.ShortCut;
import com.svmuu.AppDelegate;
import com.svmuu.R;

import java.io.File;

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
                R.drawable.loading,
                R.drawable.loading,
                R.drawable.loading
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
        enterMain();
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
