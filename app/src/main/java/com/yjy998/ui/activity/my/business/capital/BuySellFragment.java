package com.yjy998.ui.activity.my.business.capital;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.JsonUtil;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.adapter.ContractListAdapter;
import com.yjy998.entity.Contract;
import com.yjy998.entity.ContractDetail;
import com.yjy998.entity.Stock;
import com.yjy998.http.Response;
import com.yjy998.http.YHttpClient;
import com.yjy998.http.YHttpHandler;
import com.yjy998.ui.activity.other.BaseFragment;
import com.yjy998.ui.view.RoundButton;

import java.util.ArrayList;

/**
 * 买入&卖出
 */
public class BuySellFragment extends BaseFragment implements View.OnClickListener {
    View layout;
    ListPopupWindow contractListWindow;
    TimeLineFragment mTimeLineFragment = new TimeLineFragment();
    CapitalInfo mCapitalInfo;
    private TextView usableText;
    private TextView balanceText;
    private TextView stockValueText;
    private TextView totalText;
    private RoundButton buySellButton;
    private RoundButton resetButton;
    private TextView stockName;
    private TextView floatBalance;
    private TextView holdNumber;
    private TextView holdValue;
    private TextView contractText;
    private boolean isBuy;
    private ContractObserver observer;

    /**
     * 根据isBuy来执行不同的逻辑
     *
     * @param isBuy true 买入,false 卖出
     * @return CapitalFragment实例
     */
    public static BuySellFragment newInstance(boolean isBuy) {
        BuySellFragment fragment = new BuySellFragment();
        fragment.setBuy(isBuy);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ContractObserver) {
            observer = (ContractObserver) activity;
        }
    }

    public void setBuy(boolean isBuy) {
        this.isBuy = isBuy;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (layout != null) {
            //ViewPager会摧毁View
            ((ViewGroup) layout.getParent()).removeView(layout);
        } else {
            layout = inflater.inflate(R.layout.fragment_capital, null);
            mCapitalInfo = CapitalInfo.newInstance(isBuy);
        }

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        if (!mCapitalInfo.isAdded())
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, mCapitalInfo)
                    .add(R.id.fragmentContainer, mTimeLineFragment)
                    .hide(mTimeLineFragment)
                    .commit();
    }

    @Override
    public String getTitle() {
        return ContextUtil.getString(R.string.capital);
    }

    /**
     * 初始化
     */
    private void initialize() {
        if (usableText != null && usableText.getParent() == layout) {
            //防止重复初始化
            return;
        }
        usableText = (TextView) findViewById(R.id.usableText);
        balanceText = (TextView) findViewById(R.id.balanceText);
        stockValueText = (TextView) findViewById(R.id.stockValueText);
        totalText = (TextView) findViewById(R.id.totalText);
        buySellButton = (RoundButton) findViewById(R.id.buySellButton);
        resetButton = (RoundButton) findViewById(R.id.resetButton);
        stockName = (TextView) findViewById(R.id.stockName);
        floatBalance = (TextView) findViewById(R.id.floatBalance);
        holdNumber = (TextView) findViewById(R.id.holdNumber);
        holdValue = (TextView) findViewById(R.id.holdValue);
        contractText = (TextView) findViewById(R.id.contractText);
        findViewById(R.id.switchButton).setOnClickListener(this);
        findViewById(R.id.chooseContract).setOnClickListener(this);
        buySellButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        if (isBuy) {
            buySellButton.setText(R.string.buyIn);
        } else {
            buySellButton.setText(R.string.sellOut);
        }

        setContract(getSharedContract());

    }

    /**
     * 获取共享的合约
     */
    ContractDetail getSharedContract() {
        if (getActivity() instanceof ContractObserver) {
            return ((ContractObserver) getActivity()).getContract();
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switchButton: {
                //切换分时图
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                if (mTimeLineFragment.isHidden()) {
                    ft.show(mTimeLineFragment).hide(mCapitalInfo);
                    mTimeLineFragment.setStockCode(mCapitalInfo.getStockCode());
                } else {
                    ft.show(mCapitalInfo).hide(mTimeLineFragment);
                }
                ft.commit();

                break;
            }
            case R.id.resetButton: {
                mCapitalInfo.reset();
                break;
            }
            case R.id.buySellButton: {
                if (isBuy) {

                } else {

                }
                break;
            }
            case R.id.chooseContract: {
                if (contractListWindow == null) {
                    contractListWindow = new ListPopupWindow(getActivity());
                    contractListWindow.setAnchorView(v);
                    ArrayList<Contract> myContracts = AppDelegate.getInstance().getUser().myContracts;
                    contractListWindow.setAdapter(new ContractListAdapter(getActivity(), myContracts));
                    contractListWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            contractListWindow.dismiss();
                            ArrayList<Contract> myContracts = AppDelegate.getInstance().getUser().myContracts;
                            Contract contract = myContracts.get(position);
                            getContractInfo(contract.id);
                        }
                    });
                }
                contractListWindow.show();
                break;
            }
        }
    }

    public void getContractInfo(String contract_id) {
        SRequest request = new SRequest("");
        YHttpClient.getInstance().get(request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                ContractDetail detail = JsonUtil.get(response.data, ContractDetail.class);
                setContract(detail);
                notifyContractChange(detail);
            }
        });
    }

    public void setContract(ContractDetail contract) {
        if (contract != null) {
            usableText.setText(contract.currentCash);
            balanceText.setText(contract.totalProfit);
            stockValueText.setText(contract.totalMarketValue);
            totalText.setText(contract.totalAsset);
            contractText.setText(getString(R.string.contract_s1_s2, contract.contract_type, contract.contractId));
        } else {
            usableText.setText("0");
            balanceText.setText("0");
            stockValueText.setText("0");
            totalText.setText("0");
            contractText.setText(R.string.chooseContract);
        }

    }

    public void notifyContractChange(ContractDetail contract) {
        if (observer != null) {
            observer.setContract(contract);
        }
    }


    /**
     * 买入
     */
    public void buy() {
        SRequest request = new SRequest("http://www.yjy998.com/stock/buystock");
        request.put("contract_no", "");
        YHttpClient.getInstance().post(getActivity(), request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {

            }
        });
    }

    /**
     * 卖出
     */
    public void sell() {
    }

    public static interface ContractObserver {
        public ContractDetail getContract();

        public void setContract(ContractDetail contract);

    }
}
