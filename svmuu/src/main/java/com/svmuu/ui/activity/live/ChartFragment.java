package com.svmuu.ui.activity.live;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.svmuu.R;
import com.svmuu.ui.BaseFragment;

public class ChartFragment extends BaseFragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart,container,false);
    }

    @Override
    protected void initialize() {

    }
}
