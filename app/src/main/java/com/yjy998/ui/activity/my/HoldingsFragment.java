package com.yjy998.ui.activity.my;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;
import com.yjy998.adapter.HoldingsAdapter;
import com.yjy998.entity.Hold;
import com.yjy998.ui.activity.BaseFragment;
import com.yjy998.ui.pop.CenterPopup;

import java.util.ArrayList;
import java.util.List;

/**
 * 持仓
 */
public class HoldingsFragment extends CenterListFragment {


    @Override
    public String getTitle() {
        return ContextUtil.getString(R.string.holdings);
    }

    @Override
    protected void onCreatePop(CenterPopup.PopWidget popWidget) {
        super.onCreatePop(popWidget);
    }

    @Override
    protected void onPopItemClick(CenterPopup.PopItem item) {
        super.onPopItemClick(item);
    }
}
