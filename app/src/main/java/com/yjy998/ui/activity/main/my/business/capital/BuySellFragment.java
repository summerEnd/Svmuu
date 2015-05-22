package com.yjy998.ui.activity.main.my.business.capital;


import android.app.Activity;
import android.content.DialogInterface;
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
import com.yjy998.common.util.NumberUtil;
import com.yjy998.ui.activity.admin.LoginDialog;
import com.yjy998.ui.activity.base.BaseFragment;
import com.yjy998.ui.activity.main.my.business.BusinessActivity;
import com.yjy998.ui.activity.pay.PayDialog;
import com.yjy998.ui.pop.YAlertDialog;
import com.yjy998.ui.pop.YProgressDialog;
import com.yjy998.ui.view.RoundButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * 买入&卖出
 * 这个页面灰常复杂。。。
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
    YProgressDialog yProgressDialog;

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
            //ViewPager会摧毁View,导致重复调用这个方法
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
        list.setOnItemClick(new LinearListView.OnItemClick() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                if (getActivity() instanceof BusinessActivity) {
                    ((BusinessActivity) getActivity()).pager.setCurrentItem(2);
                }
            }
        });
        findViewById(R.id.switchButton).setOnClickListener(this);
        findViewById(R.id.chooseContract).setOnClickListener(this);
        buySellButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        if (isBuy) {
            buySellButton.setText(R.string.buyIn);
        } else {
            buySellButton.setText(R.string.sellOut);
        }
        showContractInfo(0);
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

                if (observer.showLoginDialogIfNeed()) {
                    return;
                }

                if (contractListWindow == null) {
                    contractListWindow = new ListPopupWindow(getActivity());
                    contractListWindow.setAnchorView(v);
                    ArrayList<Contract> myContracts = AppDelegate.getInstance().getUser().myContracts;
                    contractListWindow.setAdapter(new ContractListAdapter(getActivity(), myContracts));
                    contractListWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            contractListWindow.dismiss();
                            showContractInfo(position);
                        }
                    });
                }
                contractListWindow.show();
                break;
            }
        }
    }

    /**
     * 展示合约信息
     */
    private void showContractInfo(int position) {
        ArrayList<Contract> myContracts = AppDelegate.getInstance().getUser().myContracts;

        if (myContracts == null || position >= myContracts.size()) {
            contractText.setText(R.string.no_contract_);
            return;
        }

        Contract contract = myContracts.get(position);
        setContractLocal(null);//清空之前的数据
        contractText.setText(getString(R.string.contract_s1_s2, contract.contract_type, contract.id));
        refreshInternal(contract.id);
    }

    /**
     * 获取合约信息
     */
    public void getContractInfo(final String contract_id) {

        if (yProgressDialog == null) {
            yProgressDialog = new YProgressDialog(getActivity());
            yProgressDialog.setMessage(getString(R.string.load_amount));
        }
        //弹出进度框，将会在getHoldings()结束时自动关闭。或者获取合约详情失败时关闭
        yProgressDialog.show();

        //由于这个接口很慢，引入缓存机制，从缓存中读取
        ContractDetail detail = (ContractDetail) observer.readContractFromCache(contract_id);

        if (detail != null) {
            //详情页面使用缓存
            setContractLocal(detail);
            notifyContractChange(detail);
            getHoldings(contract_id);
            return;
        }

        //获取合约详情
        SRequest request = new SRequest("http://mobile.yjy998.com/h5/account/contractinfo");
        request.put("contract_no", contract_id);
        YHttpClient.getInstance().get(request, new YHttpHandler(true) {
            @Override
            protected void onStatusCorrect(Response response) {
                ContractDetail detail = JsonUtil.get(response.data, ContractDetail.class);
                observer.saveContract(detail);//保存到缓存
                notifyContractChange(detail);
                getHoldings(contract_id);
            }

            @Override
            protected void onStatusFailed(Response response) {
                super.onStatusFailed(response);
                ContextUtil.toast(R.string.request_is_failed);
                yProgressDialog.dismiss();
            }

        });
    }

    /**
     * 用Contract初始化本地页面
     */
    public void setContractLocal(ContractDetail contract) {
        if (contract != null) {
            usableText.setText(contract.currentCash);
            balanceText.setText(contract.totalProfit);
            stockValueText.setText(contract.totalMarketValue);
            totalText.setText(contract.totalAsset);
            contractText.setText(AppDelegate.getInstance().getString(R.string.contract_s1_s2, contract.contract_type, contract.contractId));
        } else {
            usableText.setText("");
            balanceText.setText("");
            stockValueText.setText("");
            totalText.setText("");
            contractText.setText(R.string.chooseContract);
        }
    }

    /**
     * 把合约放到Activity中，供其他fragment获取，实现联动。
     */
    public void notifyContractChange(ContractDetail contract) {
        setContractLocal(contract);
        if (observer != null) {
            observer.setContract(contract);
        }
    }


    /**
     * 买入卖出
     */
    public void beginTrade() {
        if (observer.showLoginDialogIfNeed()) {
            return;
        }
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

        final int quantity = mCapitalInfo.getQuantity();
        if (quantity <= 0) {
            //没有输入数量
            ContextUtil.toast(getString(R.string.toast_no_amout));
            return;
        }
        float price = NumberUtil.getFloat(stock.entrust_price);
        String totalAmount = price * quantity + "";
        PayDialog payDialog = new PayDialog(getActivity(), totalAmount);
        payDialog.findViewById(R.id.remainLine).setVisibility(View.GONE);
        payDialog.findViewById(R.id.remainMoneyText).setVisibility(View.GONE);
        payDialog.setCallback(new PayDialog.Callback() {
            @Override
            public void onPay(String password, String rsa_password) {
                //根据是否为买入来设置请求的url
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
                request.put("buy_quantity", quantity);
                request.put("exchange_type", stock.exchangeType);//交易所类型：1，2
                YHttpClient.getInstance().post(getActivity(), request, new YHttpHandler() {
                    @Override
                    protected void onStatusCorrect(Response response) {
                        AppDelegate.getInstance().refreshUserInfo();
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
        if (!isVisible()) {
            return;
        }

        setContractLocal(observer.getSharedContract());
        refreshInternal(observer.getContractId());
    }

    /**
     * 调用网络接口获取数据
     *
     * @param contractId
     */
    private void refreshInternal(String contractId) {
        if (!TextUtils.isEmpty(contractId)) {
            getContractInfo(contractId);
        }

        if (mCapitalInfo != null) {
            mCapitalInfo.refresh();
        }
    }

    /**
     * 获取持仓列表
     */
    public void getHoldings(String contract_no) {
        if (getActivity() == null) {
            return;
        }
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

            @Override
            public void onFinish() {
                if (yProgressDialog != null) {
                    yProgressDialog.dismiss();
                }
            }
        });
    }

    public static interface ContractObserver {
        /**
         * 获取五个fragment共享的合约
         */
        public ContractDetail getSharedContract();

        /**
         * 将合约设置到共享里，主要是为了五个fragment共享
         */
        public void setContract(ContractDetail contract);

        /**
         * 共享的股票代码
         */
        public String getStockCode();

        /**
         * 当前共享的合约的Id
         */
        public String getContractId();

        /**
         * 根据合约id从缓存中读取合约
         */
        public Object readContractFromCache(String contract_id);

        /**
         * 将合约详情缓存到本地
         */
        public Object saveContract(ContractDetail detail);

        /**
         * 如果没登录，就会弹出登录
         */
        public boolean showLoginDialogIfNeed();
    }
}
