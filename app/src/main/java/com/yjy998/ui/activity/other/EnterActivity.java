package com.yjy998.ui.activity.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.sp.lib.common.support.adapter.GuidePagerAdapter;
import com.sp.lib.common.util.SLog;
import com.yjy998.R;
import com.yjy998.ui.activity.MainActivity;
import com.yjy998.ui.activity.YJYActivity;

public class EnterActivity extends YJYActivity implements ViewPager.OnPageChangeListener {
    ViewPager pager;
    int lastPosition;
    boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        pager = new ViewPager(this);
        setContentView(pager);
        pager.setAdapter(new GuidePagerAdapter(this, new int[]{
                R.drawable.guide_01,
                R.drawable.guide_02,
                R.drawable.guide_03
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        pager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        exit = (positionOffsetPixels == lastPosition) && !pager.canScrollHorizontally(1);
        lastPosition = positionOffsetPixels;
        SLog.debug_format("position:%d positionOffset:%f positionOffsetPixels:%d", position, positionOffset, positionOffsetPixels);
//        if (!pager.canScrollHorizontally(1)) {
//            startActivity(new Intent(this, MainActivity.class));
//            pager.setOnPageChangeListener(null);
//        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE && exit) {
            startActivity(new Intent(this, MainActivity.class));
            pager.setOnPageChangeListener(null);
        }

    }
}
