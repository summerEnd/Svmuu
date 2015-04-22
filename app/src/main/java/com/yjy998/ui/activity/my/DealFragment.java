package com.yjy998.ui.activity.my;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;
import com.yjy998.ui.activity.BaseFragment;
import com.yjy998.ui.pop.CenterPopup;

import static com.yjy998.ui.pop.CenterPopup.PopItem;
import static com.yjy998.ui.pop.CenterPopup.PopWidget;

/**
 * 成交
 */
public class DealFragment extends CenterListFragment {

    @Override
    public String getTitle() {
        return  ContextUtil.getString(R.string.deal);
    }

    @Override
    protected void onCreatePop(PopWidget popWidget) {

    }

    @Override
    protected void onPopItemClick(PopItem item) {
        super.onPopItemClick(item);
    }
}
