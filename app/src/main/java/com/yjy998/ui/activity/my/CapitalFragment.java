package com.yjy998.ui.activity.my;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;
import com.yjy998.ui.activity.BaseFragment;
import com.yjy998.ui.activity.TimeLineFragment;

/**
 * 资产
 */
public class CapitalFragment extends BaseFragment {
    View layout;
    TimeLineFragment mTimeLineFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_capital, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mTimeLineFragment == null) {
            mTimeLineFragment = new TimeLineFragment();
            getChildFragmentManager().beginTransaction().add(R.id.timeLineContainer, mTimeLineFragment).commit();
        }
    }

    @Override
    public String getTitle() {
        return ContextUtil.getString(R.string.capital);
    }
}
