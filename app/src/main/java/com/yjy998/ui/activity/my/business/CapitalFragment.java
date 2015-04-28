package com.yjy998.ui.activity.my.business;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;
import com.yjy998.ui.activity.other.BaseFragment;
import com.yjy998.ui.activity.other.TimeLineFragment;

/**
 * 资产
 */
public class CapitalFragment extends BaseFragment implements View.OnClickListener {
    View layout;
    TimeLineFragment mTimeLineFragment = new TimeLineFragment();
    CapitalInfo mCapitalInfo = new CapitalInfo();
    private TextView usableText;
    private TextView balanceText;
    private TextView stockValueText;
    private TextView totalText;
    private Button buySellButton;
    private Button resetButton;
    private TextView stockName;
    private TextView floatBalance;
    private TextView holdNumber;
    private TextView holdValue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (layout != null) {
            ((ViewGroup) layout.getParent()).removeView(layout);
        } else {
            layout = inflater.inflate(R.layout.fragment_capital, null);
        }
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        if (!mCapitalInfo.isAdded())
            getChildFragmentManager().beginTransaction().add(R.id.fragmentContainer, mCapitalInfo).add(R.id.fragmentContainer, mTimeLineFragment).hide(mTimeLineFragment).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public String getTitle() {
        return ContextUtil.getString(R.string.capital);
    }

    private void initialize() {
        if (usableText != null && usableText.getParent() == layout) {
            return;
        }
        usableText = (TextView) findViewById(R.id.usableText);
        balanceText = (TextView) findViewById(R.id.balanceText);
        stockValueText = (TextView) findViewById(R.id.stockValueText);
        totalText = (TextView) findViewById(R.id.totalText);
        buySellButton = (Button) findViewById(R.id.buySellButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        stockName = (TextView) findViewById(R.id.stockName);
        floatBalance = (TextView) findViewById(R.id.floatBalance);
        holdNumber = (TextView) findViewById(R.id.holdNumber);
        holdValue = (TextView) findViewById(R.id.holdValue);
        findViewById(R.id.switchButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switchButton: {
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                if (mTimeLineFragment.isHidden()) {
                    ft.show(mTimeLineFragment).hide(mCapitalInfo);

                } else {
                    ft.show(mCapitalInfo).hide(mTimeLineFragment);
                }
                ft.commit();

                break;
            }
        }
    }

    public static class CapitalInfo extends BaseFragment {
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.layout_capital_switch_info, null);
        }
    }

}
