package com.slib.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.sp.lib.common.support.adapter.GuidePagerAdapter;
import com.sp.lib.widget.pager.title.PageStrip;
import com.svmuu.slibdemo.R;

public class PagerTitle extends SLIBTest {

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
        int[] pages = new int[strip.getTabCount()];
        for (int i = 0; i < pages.length; i++) {
            int red = i * 60 + 50;
            int green = i * 40 + 30;
            int blue = 256 - i * 30;
            pages[i] = Color.rgb(red, green, blue);
        }
        pager.setAdapter(new GuidePagerAdapter(this, pages) {
            @Override
            public CharSequence getPageTitle(int position) {
                return "title" + position;
            }
        });
        strip.setViewPager(pager);
    }
}
