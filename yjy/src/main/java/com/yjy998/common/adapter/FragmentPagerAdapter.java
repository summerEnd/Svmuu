package com.yjy998.common.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.yjy998.ui.activity.base.BaseFragment;

import java.util.ArrayList;


public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    ArrayList<BaseFragment> fragmentList = new ArrayList<BaseFragment>();

    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragmentPagerAdapter(FragmentManager fm, BaseFragment[] fragments) {
        super(fm);
        for (BaseFragment fragment : fragments) {
            add(fragment);
        }
    }

    public void add(BaseFragment f) {
        fragmentList.add(f);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentList.get(position).getTitle();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
