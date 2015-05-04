package com.yjy998.ui.activity.my.business;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.sp.lib.widget.pager.title.PageStrip;
import com.yjy998.R;
import com.yjy998.ui.activity.my.business.capital.CapitalFragment;
import com.yjy998.ui.activity.other.BaseFragment;
import com.yjy998.ui.activity.other.SecondActivity;

public class BusinessActivity extends SecondActivity {
    public static final String EXTRA_IS_BUY = "extra_buy";
    BaseFragment[] fragments = new BaseFragment[4];
    private PageStrip pageStrip;
    private ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        initialize();
    }

    private void initialize() {

        pageStrip = (PageStrip) findViewById(R.id.pageStrip);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pageStrip.setViewPager(pager);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = fragments[position];
            if (fragment == null) {
                switch (position) {
                    case 0:
                        fragment = new CapitalFragment();
                        break;
                    case 1:
                        fragment = new HoldingsFragment();
                        break;
                    case 2:
                        fragment = new CancellationEntrustFragment();
                        break;

                    case 3:
                        fragment = new DealFragment();
                        break;
                }
                fragments[position] = fragment;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
}
