package com.slib.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.sp.lib.common.support.adapter.GuidePagerAdapter;
import com.sp.lib.widget.pager.PageStrip;
import com.svmuu.slibdemo.R;

public class PagerTitle extends Activity {

    private PageStrip strip;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_title);
        initialize();
    }

    private void initialize() {

        strip = (PageStrip) findViewById(R.id.strip);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new GuidePagerAdapter(this, new int[]{R.drawable.album, R.drawable.album}));
        strip.setViewPager(pager);
    }
}
