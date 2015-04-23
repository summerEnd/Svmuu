package com.yjy998.ui.activity.game;


import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;

import static com.yjy998.ui.pop.CenterPopup.PopItem;
import static com.yjy998.ui.pop.CenterPopup.PopWidget;

/**
 * 成交
 */
public class DealFragment extends GameListFragment {

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
