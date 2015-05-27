package com.yjy998.ui.activity.main.my.business.capital;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yjy998.R;
import com.yjy998.ui.view.RoundButton;

public class BuyFragment extends TradeFragment {
    @Override
    protected CapitalInfo createCapitalFragment() {
        return CapitalInfo.newInstance(true, observer.getStockCode());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RoundButton buySellButton = (RoundButton) findViewById(R.id.buySellButton);
        buySellButton.setOnClickListener(this);
        buySellButton.setText(R.string.buyIn);

    }

    @Override
    protected String getTradeUrl() {
        return "http://www.yjy998.com/stock/buystock";
    }
}
