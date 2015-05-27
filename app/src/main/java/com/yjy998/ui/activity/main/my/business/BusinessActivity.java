package com.yjy998.ui.activity.main.my.business;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.sp.lib.common.support.cache.FileObjectCache;
import com.sp.lib.widget.pager.title.PageStrip;
import com.yjy998.R;
import com.yjy998.common.entity.ContractDetail;
import com.yjy998.ui.activity.main.my.business.capital.BuyFragment;
import com.yjy998.ui.activity.main.my.business.capital.TradeFragment;
import com.yjy998.ui.activity.base.BaseFragment;
import com.yjy998.ui.activity.base.SecondActivity;
import com.yjy998.ui.activity.main.my.business.capital.SellFragment;

import java.io.File;

public class BusinessActivity extends SecondActivity implements TradeFragment.ContractObserver, ViewPager.OnPageChangeListener {
    public static final String EXTRA_IS_BUY = "extra_buy";
    //要展示的合约的id
    public static final String EXTRA_CONTRACT_NO = "extra_contract";
    //要展示的股票的代码
    public static final String EXTRA_STOCK_CODE = "extra_stock";
    ContractDetail contract;

    BaseFragment[] fragments = new BaseFragment[5];
    public ViewPager pager;
    FileObjectCache cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        initialize();
    }

    private void initialize() {
        PageStrip pageStrip = (PageStrip) findViewById(R.id.pageStrip);
        pager = (ViewPager) findViewById(R.id.pager);
        fragments = new BaseFragment[]{
                new BuyFragment(),
                new SellFragment(),
                new HoldingsFragment(),
                new CancellationEntrustFragment(),
                new DealFragment()
        };
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
        contract = null;
        if (!isBuy) {

            if (pager.getCurrentItem() != 1) {
                pager.setCurrentItem(1);
            } else {
                fragments[1].refresh();
            }
        } else {
            if (pager.getCurrentItem() != 0) {
                pager.setCurrentItem(0);
            } else {
                ((TradeFragment) fragments[0]).getContractInfo( getContractId());
            }
        }
    }

    @Override
    public ContractDetail getSharedContract() {
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

        if (contract!=null){
            return contract.contractId;
        }

        return getIntent().getStringExtra(EXTRA_CONTRACT_NO);
    }

    @Override
    public ContractDetail readContractFromCache(String contract_id) {
        if (cache == null) {
            File dir = new File(getCacheDir(), "contract");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            cache = new FileObjectCache(dir);
        }
        return (ContractDetail) cache.read(contract_id);
    }

    @Override
    public Object saveContract(ContractDetail detail) {
        if (cache == null) {
            File dir = new File(getCacheDir(), "contract");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            cache = new FileObjectCache(dir);
        }
        return cache.write(detail.contractId, detail);
    }

    @Override
    public void clearCache() {
        if (cache != null) {
            cache.clear();
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
        if (fragments[i].isVisible()) {
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

            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       clearCache();
    }

}
