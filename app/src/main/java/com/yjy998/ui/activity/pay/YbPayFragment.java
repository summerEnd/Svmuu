package com.yjy998.ui.activity.pay;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;
import com.yjy998.ui.activity.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class YbPayFragment extends BaseFragment {


    @Override
    public String getTitle() {
        return ContextUtil.getString(R.string.onlinePay_yb);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_yb_pay, container, false);
    }


}
