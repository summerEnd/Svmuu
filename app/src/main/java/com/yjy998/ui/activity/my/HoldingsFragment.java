package com.yjy998.ui.activity.my;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;
import com.yjy998.ui.activity.BaseFragment;

/**
 * 持仓
 */
public class HoldingsFragment extends BaseFragment {

    View layout;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_holdings, container, false);
        listView= (ListView) layout.findViewById(R.id.list);
        return layout;
    }

    @Override
    public String getTitle() {
        return ContextUtil.getString(R.string.holdings);
    }

}
