package com.yjy998.ui.activity.my;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.widget.list.refresh.PullToRefreshListView;
import com.yjy998.R;
import com.yjy998.adapter.HoldingsAdapter;
import com.yjy998.entity.Hold;
import com.yjy998.ui.activity.BaseFragment;
import com.yjy998.ui.pop.CenterPopup;

import java.util.ArrayList;
import java.util.List;

import static com.yjy998.ui.pop.CenterPopup.PopItem;
import static com.yjy998.ui.pop.CenterPopup.PopWidget;

/**
 * 委托/撤单
 */
public class CancellationEntrustFragment extends CenterListFragment {


    @Override
    public String getTitle() {
        return ContextUtil.getString(R.string.cancellationEntrust);
    }

    @Override
    protected void onCreatePop(PopWidget popWidget) {
        popWidget.add(new PopItem(1,getString(R.string.cancelOrder), R.drawable.bitmap_blue_button));
        popWidget.add(new PopItem(2,getString(R.string.cancelAndRebuy), R.drawable.bitmap_red_button));
    }

    @Override
    protected void onPopItemClick(PopItem item) {
        ContextUtil.toast_debug(item.text);
    }
}
