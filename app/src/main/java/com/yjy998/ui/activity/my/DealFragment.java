package com.yjy998.ui.activity.my;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;
import com.yjy998.ui.activity.BaseFragment;

/**
 * 成交
 */
public class DealFragment extends BaseFragment {


    public DealFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deal, container, false);
    }

    @Override
    public String getTitle() {
        return  ContextUtil.getString(R.string.deal);
    }
}
