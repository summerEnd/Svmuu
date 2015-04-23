package com.yjy998.ui.activity.my;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yjy998.R;
import com.yjy998.ui.activity.BaseFragment;


public class CenterFragment extends BaseFragment {

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
