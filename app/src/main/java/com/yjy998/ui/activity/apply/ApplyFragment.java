package com.yjy998.ui.activity.apply;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sp.lib.common.support.adapter.SAdapter;
import com.yjy998.R;
import com.yjy998.ui.activity.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class ApplyFragment extends BaseFragment implements View.OnClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_apply, container, false);
        initialize(layout);
        return layout;
    }

    private void initialize(View layout) {
        layout.findViewById(R.id.t9Layout).setOnClickListener(this);
        layout.findViewById(R.id.tnLayout).setOnClickListener(this);
        layout.findViewById(R.id.freshLayout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.t9Layout: {
                startActivity(new Intent(getActivity(), ApplyActivity.class));
                break;
            }
            case R.id.tnLayout: {
                break;
            }
            case R.id.freshLayout: {
                break;
            }
        }
    }
}
