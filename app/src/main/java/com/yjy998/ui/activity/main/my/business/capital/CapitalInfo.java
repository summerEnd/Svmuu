package com.yjy998.ui.activity.main.my.business.capital;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.widget.list.LinearListView;
import com.yjy998.R;
import com.yjy998.common.adapter.CapitalBuySellAdapter;
import com.yjy998.common.adapter.StockListAdapter;
import com.yjy998.common.entity.ContractDetail;
import com.yjy998.common.entity.Stock;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.base.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.yjy998.ui.activity.main.my.business.capital.BuySellFragment.ContractObserver;

public class CapitalInfo extends BaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    public static final String IS_BUY = "isBuy";
    public static final String STOCK_CODE = "stockCode";
    /**
     * 价格最小单元
     */
    private final float PRICE_UNIT = 0.01f;
    /**
     * 数量最小单元
     */
    private final int COUNT_UNIT = 100;
    private int count = 0;
    private EditText codeEdit;
    private EditText editPrice;
    private EditText amountEdit;
    private SeekBar seeker;
    private LinearListView list;
    CapitalBuySellAdapter adapter;
    private Stock mStock;
    private ListPopupWindow stockListWindow;
    private StockListAdapter stockListAdapter;
    private boolean isStockSelected = false;
    /**
     * 最大买卖数量
     */
    private int maxAmount;
    private TextWatcher mWatcher;
    private View layout;

    /**
     * 根据字段创建买入或者卖出
     */
    public static CapitalInfo newInstance(boolean isBuy, String stockCode) {
        CapitalInfo fragment = new CapitalInfo();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_BUY, isBuy);
        bundle.putString(STOCK_CODE, stockCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (layout == null) {
            layout = inflater.inflate(R.layout.layout_capital_switch_info, null);
        } else {
            ((ViewGroup) layout.getParent()).removeView(layout);
        }
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    private void initialize() {

        if (editPrice != null) {
            return;
        }

        Bundle args = getArguments();
        if (args == null) {
            return;
        }

        editPrice = (EditText) findViewById(R.id.editPrice);
        amountEdit = (EditText) findViewById(R.id.amountEdit);
        TextView seekerTitle = (TextView) findViewById(R.id.seekerTitle);

        findViewById(R.id.addAmount).setOnClickListener(this);
        findViewById(R.id.reduceAmount).setOnClickListener(this);
        findViewById(R.id.addPrice).setOnClickListener(this);
        findViewById(R.id.reducePrice).setOnClickListener(this);

        //进度条
        seeker = (SeekBar) findViewById(R.id.seeker);
        seeker.setOnSeekBarChangeListener(this);

        //初始化持仓列表
        boolean isBuy = args.getBoolean(IS_BUY);
        list = (LinearListView) findViewById(R.id.list);
        adapter = new CapitalBuySellAdapter(getActivity(), null);
        adapter.setBuy(isBuy);
        list.setAdapter(adapter);

        //买入卖出初始化
        if (isBuy) {
            editPrice.setHint(R.string.buyPrice);
            amountEdit.setHint(R.string.buyAmount);
            seekerTitle.setText(R.string.canBuyAmount);
        } else {
            editPrice.setHint(R.string.sellPrice);
            amountEdit.setHint(R.string.sellAmount);
            seekerTitle.setText(R.string.canSellAmount);
        }
        stockListAdapter = new StockListAdapter(getActivity(), new ArrayList<Stock>());
        //初始化股票代码
        String stockCode = args.getString(STOCK_CODE);
        codeEdit = (EditText) findViewById(R.id.codeEdit);
        mWatcher = new CodeEditTextWatcher();
        codeEdit.addTextChangedListener(mWatcher);
        codeEdit.setText(stockCode);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.reduceAmount: {
                addCount(-COUNT_UNIT);
                break;
            }
            case R.id.addAmount: {
                addCount(COUNT_UNIT);
                break;
            }
            case R.id.reducePrice: {
                addPrice(-PRICE_UNIT);
                break;
            }
            case R.id.addPrice: {
                addPrice(PRICE_UNIT);
                break;
            }
        }
    }

    //加价格
    public void addPrice(float dPrice) {
        String val = editPrice.getText().toString();
        if (TextUtils.isEmpty(val)) {
            val = "0";
        }
        //解决float加减法精度丢失的问题
        BigDecimal _price = new BigDecimal(val);
        BigDecimal _dPrice = new BigDecimal(Float.toString(dPrice));
        editPrice.setText("" + Math.max(0, _price.add(_dPrice).floatValue()));

    }

    //加数量
    public void addCount(int dAmount) {
        count += dAmount;
        count = Math.max(0, count);
        amountEdit.setText("" + count);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        count = progress * 100;
        addCount(0);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * 获取共享的合约
     */
    ContractDetail getSharedContract() {
        if (getActivity() instanceof ContractObserver) {
            return ((ContractObserver) getActivity()).getSharedContract();
        }
        return null;
    }

    /**
     * 获取可买数量
     */
    public void buyQuantity() {
        ContractDetail contract = getSharedContract();
        Stock stock = getStock();

        if (contract == null) {
//            if (isVisible())ContextUtil.toast(R.string.contract_not_selected);
            return;
        }

        if (stock == null) {
//           if (isVisible())ContextUtil.toast(R.string.stock_not_selected);
            return;
        }

        SRequest request = new SRequest("http://www.yjy998.com/stock/buyquantity");
        request.put("entrust_price", stock.entrust_price);
        request.put("contract_id", contract.contractId);
        request.put("exchange_type", stock.exchangeType);
        request.put("stock_code", stock.stockCode);
        YHttpClient.getInstance().post(getActivity(), request, new YHttpHandler(false) {
            @Override
            protected void onStatusCorrect(Response response) {
                try {
                    maxAmount = Integer.parseInt(response.data);
                    seeker.setMax(maxAmount / 100);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取股票代码
     */
    public String getStockCode() {
        if (codeEdit == null) {
            return null;
        }
        return codeEdit.getText().toString();
    }

    /**
     * 获取数量
     */
    public String getQuantity() {
        return amountEdit.getText().toString();
    }

    public Stock getStock() {
        if (mStock != null) {
            mStock.entrust_price = editPrice.getText().toString();
        }
        return mStock;
    }

    /**
     * 证券代码编辑监听器
     */
    private class CodeEditTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            YHttpClient.getInstance().cancel(getActivity());
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String code = s.toString();
            if (TextUtils.isEmpty(code)) {
                if (stockListWindow != null) {
                    stockListWindow.dismiss();
                }
                return;
            }

            if (code.length() < 6) {
                isStockSelected = false;
            }

            if (!isStockSelected) {
                getStockInfo(code);
            }
        }
    }

    private void initStockListWindow() {
        if (stockListWindow == null) {
            stockListWindow = new ListPopupWindow(getActivity());
            stockListWindow.setAnchorView(codeEdit);
            stockListWindow.setAdapter(stockListAdapter);
            stockListWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    isStockSelected = true;
                    Stock stock = stockListAdapter.getData().get(position);
                    codeEdit.setText(stock.stockCode);
                    //这里的stock只是一个简要的股票信息，需要重新调接口获取详情
                    getStockPrice(stock.stockCode);
                    stockListWindow.dismiss();
                }
            });
        }
    }

    /**
     * 获取股票五买五卖信息
     *
     * @param code 6位完整的股票代码
     */
    private void getStockPrice(String code) {
        YHttpClient.getInstance().getStockPrice(code, new YHttpHandler(false) {
            @Override
            protected void onStatusCorrect(Response response) {

                try {
                    //服务端可能返回json数组，会报异常
                    setStock(JsonUtil.get(response.data, Stock.class));
                } catch (Exception e) {

                }


            }
        });
    }

    /**
     * 根据code进行模糊查询，获取股票列表
     *
     * @param code 小于等于六位
     */
    private void getStockInfo(String code) {

        if (code == null || code.length() >= 6) {

        } else {
            SRequest request = new SRequest("http://www.yjy998.com/stock/getstockinfo");
            request.put("code", code);
            YHttpClient.getInstance().get(request, new YHttpHandler(false) {
                @Override
                protected void onStatusCorrect(Response response) {
                    try {

                        List<Stock> data = stockListAdapter.getData();
                        data.clear();
                        JsonUtil.getArray(new JSONArray(response.data), Stock.class, data);

                        stockListAdapter.notifyDataSetChanged();
                        initStockListWindow();
                        stockListWindow.show();

                    } catch (JSONException e1) {

                    }

                }
            });
        }
    }

    public void reset() {
        codeEdit.setText("");
        editPrice.setText("");
        amountEdit.setText("");
    }

    private void setStock(Stock stock) {
        this.mStock = stock;
        adapter.setStock(stock);
        editPrice.setText(stock.newPrice);
        buyQuantity();
    }

    @Override
    public void refresh() {
        if (codeEdit == null) {
            return;
        }
        String stockCode = ((ContractObserver) getActivity()).getStockCode();
        if (mWatcher != null) {
            codeEdit.removeTextChangedListener(mWatcher);
        }
        if (!TextUtils.isEmpty(stockCode)){
            codeEdit.setText(stockCode);
        }
        codeEdit.addTextChangedListener(mWatcher);
        if (!TextUtils.isEmpty(stockCode)) {
            getStockPrice(stockCode);
        }

    }
}
