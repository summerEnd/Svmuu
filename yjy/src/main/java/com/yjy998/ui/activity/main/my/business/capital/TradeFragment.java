package com.yjy998.ui.activity.main.my.business.capital;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.widget.list.LinearListView;
import com.sp.lib.widget.list.refresh.PullToRefreshBase;
import com.sp.lib.widget.list.refresh.PullToRefreshScrollView;
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
import com.yjy998.ui.activity.base.BaseFragment;
import com.yjy998.ui.activity.main.my.business.BusinessActivity;
import com.yjy998.ui.activity.pay.PayDialog;
import com.yjy998.ui.pop.YAlertDialog;
import com.yjy998.ui.pop.YProgressDialog;
import com.yjy998.ui.view.RoundButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 买入&卖出
 * 这个页面灰常复杂。。。
 */
public abstract class TradeFragment extends BaseFragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener<ScrollView> {
    PullToRefreshScrollView layout;
    ListPopupWindow contractsWindow;
    TimeLineFragment mTimeLineFragment = new TimeLineFragment();
    protected CapitalInfo mCapitalInfo;
    private TextView usableText;
    private TextView balanceText;
    private TextView stockValueText;
    private TextView totalText;

    private TextView contractText;
    private LinearListView list;
    protected ContractObserver observer;
    ContractStockListAdapter contractStockListAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Activity必须实现这个接口
        observer = (ContractObserver) activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (layout != null) {
            //ViewPager会摧毁View,导致重复调用这个方法
            ViewGroup parent = (ViewGroup) layout.getParent();
            if(parent!=null){
                parent.removeView(layout);
            }

        } else {
            layout = new PullToRefreshScrollView(getActivity());
            inflater.inflate(R.layout.refreshable_capital, layout.getRefreshableView());
            layout.setBackgroundColor(Color.WHITE);
            layout.setOnRefreshListener(this);
            layout.setVerticalScrollBarEnabled(false);
            layout.getHeaderLoadingLayout().setRefreshingLabel(getString(R.string.load_amount));
            mCapitalInfo = createCapitalFragment();
        }
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        ContractDetail sharedContract = observer.getSharedContract();
        String sharedId = sharedContract == null ? null : sharedContract.contractId;

        if (isDoRefreshWhenCreated()) {
            setDataWithId(sharedId);
            setDoRefreshWhenCreated(false);
        }
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
        RoundButton resetButton = (RoundButton) findViewById(R.id.resetButton);
        contractText = (TextView) findViewById(R.id.contractText);
        list = (LinearListView) findViewById(R.id.list);
        contractStockListAdapter = new ContractStockListAdapter(getActivity(), new ArrayList<Holding>());
        list.setAdapter(contractStockListAdapter);
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
        resetButton.setOnClickListener(this);

        if (!mCapitalInfo.isAdded()) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, mCapitalInfo)
                    .add(R.id.fragmentContainer, mTimeLineFragment)
                    .hide(mTimeLineFragment)
                    .commit();
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        observer.clearCache();
        refresh();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    protected abstract CapitalInfo createCapitalFragment();


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
                mTimeLineFragment.reset();
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

                if (contractsWindow == null) {
                    contractsWindow = new ListPopupWindow(getActivity());
                    contractsWindow.setAnchorView(v);
                    ArrayList<Contract> myContracts = AppDelegate.getInstance().getUser().myContracts;
                    contractsWindow.setAdapter(new ContractListAdapter(getActivity(), myContracts));
                    contractsWindow.setOnItemClickListener(new OnContractClick());
                }
                contractsWindow.show();
                break;
            }
        }
    }

    /**
     * 合约列表点击事件
     */
    private class OnContractClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            contractsWindow.dismiss();
            List<Contract> myContracts = AppDelegate.getInstance().getUser().myContracts;
            if (myContracts == null || position >= myContracts.size()) {
                contractText.setText(R.string.no_contract_);
                return;
            }
            Contract contract = myContracts.get(position);
            setContractLocal(null);//清空之前的数据
            contractText.setText(getString(R.string.contract_s1_s2, contract.contract_name, contract.id));
            ContractDetail sharedContract = new ContractDetail();
            sharedContract.contract = contract;
            sharedContract.contractId = contract.contract_no;
            sharedContract.contract_type = contract.contract_type;
            observer.setContract(sharedContract);

            setDataWithId(contract.id);

        }
    }


    /**
     * 用Contract初始化本地页面
     */
    public void setContractLocal(ContractDetail contract) {

        if (getView() == null) {
            return;
        }

        if (contract != null) {
            usableText.setText(contract.currentCash);
            balanceText.setText(contract.totalProfit);
            stockValueText.setText(contract.totalMarketValue);
            totalText.setText(contract.totalAsset);
            if (!TextUtils.isEmpty(contract.contractName) && !TextUtils.isEmpty(contract.contractId))
                contractText.setText(getString(R.string.contract_s1_s2, contract.contractName, contract.contractId));
        } else {
            usableText.setText("");
            balanceText.setText("");
            stockValueText.setText("");
            totalText.setText("");

            ArrayList<Contract> myContracts = AppDelegate.getInstance().getUser().myContracts;
            if (myContracts == null || myContracts.size() == 0) {
                contractText.setText(R.string.no_contract);
            } else {
                contractText.setText(R.string.chooseContract);
            }
        }
    }

    /**
     * 把合约放到Activity中，供其他fragment获取，实现联动。
     */

    public void notifyContractChange(ContractDetail contract) {
        if (observer != null) {
            observer.setContract(contract);
            observer.saveContract(contract);//保存到缓存
        }
    }


    /**
     * 开始交易
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
        final ContractDetail detail = observer.getSharedContract();

        if (detail == null) {
            ContextUtil.toast(getString(R.string.contract_not_selected));
            return;
        }

        BigDecimal maxPrice=new BigDecimal(stock.maxPrice);
        BigDecimal minPrice=new BigDecimal(stock.minPrice);
        BigDecimal price=new BigDecimal(stock.entrust_price);

        if (price.compareTo(maxPrice)>0||price.compareTo(minPrice)<0){
            ContextUtil.toast(getString(R.string.price_invalid));
            return;
        }


        String totalAmount = new BigDecimal(stock.entrust_price).multiply(new BigDecimal(quantity)).toString();
        final PayDialog payDialog = new PayDialog(getActivity(), totalAmount);
        payDialog.findViewById(R.id.remainLine).setVisibility(View.GONE);
        payDialog.findViewById(R.id.remainMoneyText).setVisibility(View.GONE);
        payDialog.setCallback(new PayDialog.Callback() {
            @Override
            public void onPay(String password, String rsa_password) {

                SRequest request = new SRequest(getTradeUrl());
                request.put("contract_no", detail.contractId);
                request.put("contract_id", detail.contractId);
                request.put("raw_pwd", password);
                request.put("pay_pwd", rsa_password);
                request.put("stock_code", stock.stockCode);
                request.put("stock_name", stock.stockName);
                request.put("buy_price", stock.entrust_price);
                request.put("sell_price", stock.entrust_price);
                request.put("buy_quantity", quantity);
                request.put("sell_quantity", quantity);
                request.put("exchange_type", stock.exchangeType);//交易所类型：1，2
                YHttpClient.getInstance().post(getActivity(), request, new YHttpHandler() {
                    @Override
                    protected void onStatusCorrect(Response response) {
                        payDialog.dismiss();
                        AppDelegate.getInstance().refreshUserInfo();
                        ContextUtil.toast(getString(R.string.trade_ok));
                    }

                    @Override
                    protected void onStatusFailed(Response response) {
                        super.onStatusFailed(response);
                        YAlertDialog.show(getActivity(), response.message);
                    }
                });
            }
        });
        payDialog.show();
    }

    protected abstract String getTradeUrl();

    @Override
    public void refresh() {
        if (getView() == null) {
            //视图还没创建好，延迟刷新
            setDoRefreshWhenCreated(true);
            return;
        }

        ContractDetail sharedContract = observer.getSharedContract();
        setContractLocal(sharedContract);
        setDataWithId(sharedContract == null ? null : sharedContract.contractId);
    }

    /**
     * 利用合约id刷新界面，优先使用缓存
     */
    public void setDataWithId(String contractId) {

        if (TextUtils.isEmpty(contractId)) {
            //不允许为空
            return;
        }

        if (getActivity() != null) {
            //由于这个接口很慢，引入缓存机制，从缓存中读取
            ContractDetail detail = (ContractDetail) observer.readContractFromCache(contractId);

            if (detail != null) {
                //详情页面使用缓存
                setContractLocal(detail);
                notifyContractChange(detail);

                ArrayList<Holding> holdingArrayList = detail.holdings;
                if (holdingArrayList == null || holdingArrayList.size() == 0) {
                    getHoldings(detail.contractId);
                } else {
                    contractStockListAdapter.getData().clear();
                    contractStockListAdapter.getData().addAll(holdingArrayList);
                    contractStockListAdapter.notifyDataSetChanged();
                }
            } else {
                getContractInfo(contractId);
            }
        }

        if (mCapitalInfo != null) {
            mCapitalInfo.refresh();
        }
    }

    /**
     * 获取合约信息,这个接口很慢.
     */
    public void getContractInfo(final String contract_id) {

        if (TextUtils.isEmpty(contract_id)) {
            //合约id不能为空
            return;
        }

        //获取合约详情
        SRequest request = new SRequest("http://mobile.yjy998.com/h5/account/contractinfo");
        request.put("contract_no", contract_id);
        YHttpClient.getInstance().get(request, new YHttpHandler(false) {
            @Override
            protected void onStatusCorrect(Response response) {
                ContractDetail detail = JsonUtil.get(response.data, ContractDetail.class);
                detail.contract = observer.getSharedContract().contract;
                setContractLocal(detail);
                notifyContractChange(detail);
                getHoldings(contract_id);
            }

            @Override
            protected void onStatusFailed(Response response) {
                super.onStatusFailed(response);
                ContextUtil.toast(R.string.request_is_failed);


                if (layout != null)
                    layout.onPullDownRefreshComplete();
            }

            @Override
            public Dialog onCreateDialog() {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    YProgressDialog dialog = new YProgressDialog(activity);
                    dialog.setMessage(getString(R.string.load_amount));
                    return dialog;
                }
                return super.onCreateDialog();
            }
        });
    }

    /**
     * 获取持仓列表：
     * 获取持仓列表，并把其放入ContractDetail缓存起来
     */
    public void getHoldings(String contract_no) {
        if (getActivity() == null) {
            if (layout != null)
                layout.onPullDownRefreshComplete();
            return;
        }
        SRequest request = new SRequest("http://www.yjy998.com/stock/hold");
        request.put("contract_no", contract_no);
        YHttpClient.getInstance().get(getActivity(), request, new YHttpHandler(false) {
            @Override
            protected void onStatusCorrect(Response response) {
                try {
                    JSONArray array = new JSONArray(response.data);

                    ArrayList<Holding> holdingList = (ArrayList<Holding>) contractStockListAdapter.getData();
                    holdingList.clear();
                    JsonUtil.getArray(array, Holding.class, holdingList);
                    contractStockListAdapter.notifyDataSetChanged();
                    ContractDetail sharedContract = observer.getSharedContract();
                    sharedContract.holdings = holdingList;
                    notifyContractChange(sharedContract);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                if (layout != null) {
                    layout.onPullDownRefreshComplete();
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

        public void clearCache();
    }
}
