package com.yjy998.ui.activity.apply;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yjy998.R;
import com.yjy998.ui.activity.other.BaseFragment;

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
                startActivity(new Intent(getActivity(), ApplyActivity.class)
                        .putExtra(ApplyActivity.EXTRA_APPLY, ApplyActivity._T9));
                break;
            }
            case R.id.tnLayout: {
                startActivity(new Intent(getActivity(), ApplyActivity.class)
                        .putExtra(ApplyActivity.EXTRA_APPLY, ApplyActivity._TN));
                break;
            }
            case R.id.freshLayout: {
                break;
            }
        }
    }
}
