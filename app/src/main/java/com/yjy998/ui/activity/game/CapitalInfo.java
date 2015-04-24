package com.yjy998.ui.activity.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yjy998.R;
import com.yjy998.ui.activity.other.BaseFragment;

public class CapitalInfo extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_capital_switch_info, null);
    }
}
