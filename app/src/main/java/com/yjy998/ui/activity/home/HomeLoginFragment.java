package com.yjy998.ui.activity.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sp.lib.common.support.IntentFactory;
import com.sp.lib.common.util.ViewFinder;
import com.yjy998.R;
import com.yjy998.ui.activity.BaseFragment;

/**
 * 已登录过的首页
 */
public class HomeLoginFragment extends BaseFragment {

    View layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_home_logined, container, false);
        init(layout);
        return layout;
    }

    void init(View v) {

    }

}
