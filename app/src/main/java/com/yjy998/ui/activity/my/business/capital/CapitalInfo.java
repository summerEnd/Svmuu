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

import com.sp.lib.widget.list.LinearListView;
import com.yjy998.R;
import com.yjy998.adapter.CapitalBuySellAdapter;
import com.yjy998.entity.Stock;
import com.yjy998.http.Response;
import com.yjy998.http.YHttpClient;
import com.yjy998.http.YHttpHandler;
import com.yjy998.ui.activity.other.BaseFragment;

public class CapitalInfo extends BaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private EditText codeEdit;
    private EditText editPrice;
    private EditText amountEdit;
    private SeekBar seeker;
    private LinearListView list;
    private boolean isBuy;

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
        CapitalBuySellAdapter adapter = new CapitalBuySellAdapter(getActivity());
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
                int amount = Integer.parseInt(amountEdit.getText().toString());
                amount -= 100;
                amountEdit.setText("" + Math.max(0, amount));
                break;
            }
            case R.id.addAmount: {
                int amount = Integer.parseInt(amountEdit.getText().toString());
                amount += 100;
                amountEdit.setText("" + amount);
                break;
            }
            case R.id.reducePrice: {
                float price = Float.parseFloat(editPrice.getText().toString());
                price -= 0.01;
                editPrice.setText("" + Math.max(0, price));
                break;
            }
            case R.id.addPrice: {
                float price = Float.parseFloat(editPrice.getText().toString());
                price += 0.01;
                editPrice.setText("" + price);
                break;
            }
        }
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
            YHttpClient.getInstance().cancelAll();
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

                }
            });
        }
    }
}
