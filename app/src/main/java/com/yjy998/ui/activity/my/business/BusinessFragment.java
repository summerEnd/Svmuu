package com.yjy998.ui.activity.my.business;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sp.lib.common.util.ViewFinder;
import com.sp.lib.widget.pager.title.PageStrip;
import com.yjy998.R;
import com.yjy998.ui.activity.my.business.capital.CapitalFragment;
import com.yjy998.ui.activity.other.BaseFragment;


public class BusinessFragment extends BaseFragment {


    View layout;
    private PageStrip pageStrip;
    private ViewPager pager;
    BaseFragment[] fragments = new BaseFragment[4];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.activity_business, container, false);
        initialize(layout);
        return layout;
    }


    private void initialize(View v) {
        ViewFinder layout = new ViewFinder(v);
        pageStrip = layout.findView(R.id.pageStrip);
        pager = layout.findView(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        pageStrip.setViewPager(pager);
    }

    private class MyPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

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
