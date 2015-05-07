package com.yjy998.ui.activity.my.business;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.sp.lib.widget.pager.title.PageStrip;
import com.yjy998.R;
import com.yjy998.entity.Contract;
import com.yjy998.ui.activity.my.business.capital.BuySellFragment;
import com.yjy998.ui.activity.other.BaseFragment;
import com.yjy998.ui.activity.other.SecondActivity;

public class BusinessActivity extends SecondActivity implements BuySellFragment.ContractObserver, ViewPager.OnPageChangeListener {
    public static final String EXTRA_IS_BUY = "extra_buy";
    Contract contract;

    BaseFragment[] fragments = new BaseFragment[5];
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
        pageStrip.setPageChangeListener(this);
        //显示那一页
        Intent intent = getIntent();
        boolean isBuy = intent.getBooleanExtra(EXTRA_IS_BUY, false);
        if (intent != null && !isBuy) {
            pager.setCurrentItem(1);
        } else {
            pager.setCurrentItem(0);
        }
    }


    @Override
    public Contract getContract() {
        return contract;
    }

    @Override
    public void setContract(Contract contract) {
        this.contract = contract;
        if (fragments[0] instanceof BuySellFragment && fragments[0].isVisible()) {
            ((BuySellFragment) fragments[0]).setContract(contract);
        }
        if (fragments[1] instanceof BuySellFragment && fragments[1].isVisible()) {
            ((BuySellFragment) fragments[1]).setContract(contract);
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        if (fragments[i]!=null&&fragments[i].isVisible()) {
            fragments[i].refresh();
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

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
                    case 0: {
                        BuySellFragment buySellFragment = BuySellFragment.newInstance(true);
                        fragment = buySellFragment;
                    }

                    break;
                    case 1: {
                        BuySellFragment buySellFragment = BuySellFragment.newInstance(false);
                        fragment = buySellFragment;
                    }
                    break;
                    case 2:
                        fragment = new HoldingsFragment();
                        break;
                    case 3:
                        fragment = new CancellationEntrustFragment();
                        break;

                    case 4:
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
