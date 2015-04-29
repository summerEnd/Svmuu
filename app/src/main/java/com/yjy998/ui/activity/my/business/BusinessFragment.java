package com.yjy998.ui.activity.my.business;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sp.lib.common.util.ViewFinder;
import com.sp.lib.widget.PagerSlidingTabStrip;
import com.yjy998.R;
import com.yjy998.adapter.FragmentPagerAdapter;
import com.yjy998.ui.activity.other.BaseFragment;


public class BusinessFragment extends BaseFragment {


    View layout;
    private PagerSlidingTabStrip tabStrip;
    private ViewPager pager;
    BaseFragment[] fragments = new BaseFragment[]{
            new CapitalFragment(),
            new HoldingsFragment(),
            new CancellationEntrustFragment(),
            new DealFragment()
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.layout_tab_pager, container, false);
        initialize(layout);
        return layout;
    }


    private void initialize(View v) {
        ViewFinder layout = new ViewFinder(v);
        tabStrip = layout.findView(R.id.tabStrip);
        pager = layout.findView(R.id.pager);
        pager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(), fragments));
        tabStrip.setViewPager(pager);
        tabStrip.setDividerColor(getResources().getColor(R.color.textColorDeepGray));
        tabStrip.setBackgroundColor(0xfff2f2f2);
    }

}
