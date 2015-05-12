package com.yjy998.ui.activity.main.my.business.capital;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.sp.lib.common.support.cache.CacheManager;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.widget.list.LinearListView;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.common.adapter.ContractListAdapter;
import com.yjy998.common.adapter.ContractStockListAdapter;
import com.yjy998.common.entity.Contract;
import com.yjy998.common.entity.ContractDetail;
import com.yjy998.common.entity.Holding;
import com.yjy998.common.entity.Stock;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.base.BaseFragment;
import com.yjy998.ui.pop.PayDialog;
import com.yjy998.ui.pop.YProgressDialog;
import com.yjy998.ui.view.RoundButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * 买入&卖出
 */
public class BuySellFragment extends BaseFragment implements View.OnClickListener {
    public static final String CONTRACT_DETAIL = "contract_detail";
    View layout;
    ListPopupWindow contractListWindow;
    TimeLineFragment mTimeLineFragment = new TimeLineFragment();
    CapitalInfo mCapitalInfo;
    private TextView usableText;
    private TextView balanceText;
    private TextView stockValueText;
    private TextView totalText;

    private TextView contractText;
    private LinearListView list;
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
        //Activity必须实现这个接口
        observer = (ContractObserver) activity;
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
            mCapitalInfo = CapitalInfo.newInstance(isBuy, observer.getStockCode());
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
        RoundButton buySellButton = (RoundButton) findViewById(R.id.buySellButton);
        RoundButton resetButton = (RoundButton) findViewById(R.id.resetButton);
        contractText = (TextView) findViewById(R.id.contractText);
        list = (LinearListView) findViewById(R.id.list);
        findViewById(R.id.switchButton).setOnClickListener(this);
        findViewById(R.id.chooseContract).setOnClickListener(this);
        buySellButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        if (isBuy) {
            buySellButton.setText(R.string.buyIn);
        } else {
            buySellButton.setText(R.string.sellOut);
        }


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
                beginTrade();
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
                            contractText.setText(getString(R.string.contract_s1_s2, contract.contract_type, contract.id));
                            setData(null);
                            refreshInternal(contract.id);
                        }
                    });
                }
                contractListWindow.show();
                break;
            }
        }
    }

    public void getContractInfo(final String contract_id) {
        SRequest request = new SRequest("http://mobile.yjy998.com/h5/account/contractinfo");
        request.put("contract_no", contract_id);
        YHttpClient.getInstance().get(request, new YHttpHandler(true) {
            @Override
            protected void onStatusCorrect(Response response) {
                ContractDetail detail = JsonUtil.get(response.data, ContractDetail.class);
                observer.saveContract(detail);
                notifyContractChange(detail);
            }

            @Override
            public Dialog onCreateDialog() {
                ContractDetail detail = (ContractDetail) observer.readContractFromCache(contract_id);
                if (detail == null) {
                    YProgressDialog yProgressDialog = new YProgressDialog(getActivity());
                    yProgressDialog.setMessage(getString(R.string.load_amount));
                    return yProgressDialog;
                } else {
                    setData(detail);
                }
                return null;
            }

        });
    }

    public void setData(ContractDetail contract) {
        if (contract != null) {
            usableText.setText(contract.currentCash);
            balanceText.setText(contract.totalProfit);
            stockValueText.setText(contract.totalMarketValue);
            totalText.setText(contract.totalAsset);
            contractText.setText(getString(R.string.contract_s1_s2, contract.contract_type, contract.contractId));
        } else {
            usableText.setText("");
            balanceText.setText("");
            stockValueText.setText("");
            totalText.setText("");
            contractText.setText(R.string.chooseContract);
        }

    }

    public void notifyContractChange(ContractDetail contract) {
        setData(contract);
        if (observer != null) {
            observer.setContract(contract);
        }
    }


    /**
     * 买入
     * 参数：contract_no（合约id），raw_pwd（未加密支付密码），pay_pwd，stock_code（股票代码），stock_name（股票名），buy_price（买入价格），buy_quantity（买入数量），exchange_type（交易所类型：1，2）
     */
    public void beginTrade() {
        final Stock stock = mCapitalInfo.getStock();
        if (stock == null || TextUtils.isEmpty(stock.stockCode)) {
            //没有选择股票
            ContextUtil.toast(getString(R.string.toast_no_stock));
            return;
        }

        if (TextUtils.isEmpty(stock.entrust_price)) {
            //没有选择价格
            ContextUtil.toast(getString(R.string.toast_no_price));
            return;
        }

        if (TextUtils.isEmpty(mCapitalInfo.getQuantity())) {
            //没有输入数量
            ContextUtil.toast(getString(R.string.toast_no_amout));
            return;
        }

        PayDialog payDialog = new PayDialog(getActivity());
        payDialog.setCallback(new PayDialog.Callback() {
            @Override
            public void onPay(String password, String rsa_password) {
                String url = isBuy ? "http://www.yjy998.com/stock/buystock" : "http://www.yjy998.com/stock/sellstock";
                SRequest request = new SRequest(url);
                ContractDetail detail = observer.getSharedContract();
                request.put("contract_no", detail.contractId);
                request.put("contract_id", detail.contractId);
                request.put("raw_pwd", password);
                request.put("pay_pwd", rsa_password);
                request.put("stock_code", stock.stockCode);
                request.put("stock_name", stock.stockName);
                request.put("buy_price", stock.entrust_price);
                request.put("buy_quantity", mCapitalInfo.getQuantity());
                request.put("exchange_type", stock.exchangeType);
                YHttpClient.getInstance().post(getActivity(), request, new YHttpHandler() {
                    @Override
                    protected void onStatusCorrect(Response response) {

                    }
                });
            }
        });
        payDialog.show();
    }

    @Override
    public void refresh() {
        if (observer == null) {
            return;
        }
        if (!isVisible()){
            return;
        }

            setData(observer.getSharedContract());
        refreshInternal(observer.getContractId());
    }

    private void refreshInternal(String contractId) {
        if (!TextUtils.isEmpty(contractId)) {
            getContractInfo(contractId);
        }
        getHoldings(contractId);

        if (mCapitalInfo != null) {
            mCapitalInfo.refresh();
        }
    }

    public void getHoldings(String contract_no) {

        SRequest request = new SRequest("http://www.yjy998.com/stock/hold");
        request.put("contract_no", contract_no);
        YHttpClient.getInstance().get(getActivity(), request, new YHttpHandler(false) {
            @Override
            protected void onStatusCorrect(Response response) {
                try {
                    JSONArray array = new JSONArray(response.data);
                    ContractStockListAdapter adapter = new ContractStockListAdapter(getActivity(), JsonUtil.getArray(array, Holding.class));
                    list.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static interface ContractObserver {
        public ContractDetail getSharedContract();

        public void setContract(ContractDetail contract);

        public String getStockCode();

        public String getContractId();

        public Object readContractFromCache(String contract_id);

        public Object saveContract(ContractDetail detail);
    }
}
