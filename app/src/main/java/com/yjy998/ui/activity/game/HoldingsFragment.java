package com.yjy998.ui.activity.game;


import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;
import com.yjy998.ui.pop.CenterPopup;

/**
 * 持仓
 */
public class HoldingsFragment extends GameListFragment {


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
