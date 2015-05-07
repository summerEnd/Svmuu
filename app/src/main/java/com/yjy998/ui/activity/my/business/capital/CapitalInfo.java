package com.yjy998.ui.activity.my.business.capital;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.widget.list.LinearListView;
import com.yjy998.R;
import com.yjy998.adapter.CapitalBuySellAdapter;
import com.yjy998.entity.Stock;
import com.yjy998.http.Response;
import com.yjy998.http.YHttpClient;
import com.yjy998.http.YHttpHandler;
import com.yjy998.ui.activity.other.BaseFragment;

import java.math.BigDecimal;

public class CapitalInfo extends BaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
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
    private boolean isBuy;
    CapitalBuySellAdapter adapter;

    public static CapitalInfo newInstance(boolean isBuy) {
        CapitalInfo fragment = new CapitalInfo();
        fragment.setBuy(isBuy);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_capital_switch_info, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    private void initialize() {

        codeEdit = (EditText) findViewById(R.id.codeEdit);
        editPrice = (EditText) findViewById(R.id.editPrice);
        amountEdit = (EditText) findViewById(R.id.amountEdit);
        TextView seekerTitle = (TextView) findViewById(R.id.seekerTitle);
        seeker = (SeekBar) findViewById(R.id.seeker);

        list = (LinearListView) findViewById(R.id.list);
        adapter = new CapitalBuySellAdapter(getActivity(), null);
        adapter.setBuy(isBuy);
        list.setAdapter(adapter);

        findViewById(R.id.addAmount).setOnClickListener(this);
        findViewById(R.id.reduceAmount).setOnClickListener(this);
        findViewById(R.id.addPrice).setOnClickListener(this);
        findViewById(R.id.reducePrice).setOnClickListener(this);

        seeker.setOnSeekBarChangeListener(this);
        codeEdit.addTextChangedListener(new CodeEditTextWatcher());
        if (isBuy) {
            editPrice.setHint(R.string.buyPrice);
            amountEdit.setHint(R.string.buyAmount);
            seekerTitle.setText(R.string.canBuyAmount);
        } else {
            editPrice.setHint(R.string.sellPrice);
            amountEdit.setHint(R.string.sellAmount);
            seekerTitle.setText(R.string.canSellAmount);
        }
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

    public void addCount(int dAmount) {

        count += dAmount;
        count = Math.max(0, count);
        amountEdit.setText("" + count);
    }

    public void setBuy(boolean buy) {
        this.isBuy = buy;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
            if (TextUtils.isEmpty(code) || code.length() < 4) {
                return;
            }
            YHttpClient.getInstance().getStockInfo(code, new YHttpHandler(false) {
                @Override
                protected void onStatusCorrect(Response response) {
                    try {
                        //服务端可能返回json数组，会报异常
                        Stock stock = JsonUtil.get(response.data, Stock.class);
                        adapter.setStock(stock);
                        editPrice.setText(stock.newPrice);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
