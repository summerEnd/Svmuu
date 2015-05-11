package com.yjy998.ui.activity.main.my.business;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.sp.lib.common.support.cache.CacheManager;
import com.sp.lib.widget.pager.title.PageStrip;
import com.yjy998.R;
import com.yjy998.common.entity.ContractDetail;
import com.yjy998.ui.activity.main.my.business.capital.BuySellFragment;
import com.yjy998.ui.activity.base.BaseFragment;
import com.yjy998.ui.activity.base.SecondActivity;

public class BusinessActivity extends SecondActivity implements BuySellFragment.ContractObserver, ViewPager.OnPageChangeListener {
    public static final String EXTRA_IS_BUY = "extra_buy";
    //要展示的合约的id
    public static final String EXTRA_CONTRACT_NO = "extra_contract";
    //要展示的股票的代码
    public static final String EXTRA_STOCK_CODE = "extra_stock";
    ContractDetail contract;

    BaseFragment[] fragments = new BaseFragment[5];
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        initialize();
    }

    private void initialize() {
        PageStrip pageStrip = (PageStrip) findViewById(R.id.pageStrip);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pageStrip.setViewPager(pager);
        pageStrip.setPageChangeListener(this);
        parseIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        parseIntent();
    }

    void parseIntent() {
        //显示那一页
        Intent intent = getIntent();
        boolean isBuy = intent.getBooleanExtra(EXTRA_IS_BUY, false);
        if (!isBuy) {
            pager.setCurrentItem(1);
        } else {
            pager.setCurrentItem(0);
        }
    }

    @Override
    public ContractDetail getContract() {
        return contract;
    }

    @Override
    public void setContract(ContractDetail contract) {
        this.contract = contract;
    }

    @Override
    public String getStockCode() {
        return getIntent().getStringExtra(EXTRA_STOCK_CODE);
    }

    @Override
    public String getContractId() {
        return getIntent().getStringExtra(EXTRA_CONTRACT_NO);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        if (fragments[i] != null && fragments[i].isVisible()) {
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
                        fragment = BuySellFragment.newInstance(true);
                    }

                    break;
                    case 1: {
                        fragment = BuySellFragment.newInstance(false);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheManager.getInstance().remove(BuySellFragment.CONTRACT_DETAIL);
    }
}
