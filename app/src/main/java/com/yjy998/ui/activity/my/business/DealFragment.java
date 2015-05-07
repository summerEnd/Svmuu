package com.yjy998.ui.activity.my.business;


import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;
import com.yjy998.ui.pop.CenterPopup;

import static com.yjy998.ui.pop.CenterPopup.PopItem;
import static com.yjy998.ui.pop.CenterPopup.PopWidget;

/**
 * 成交
 */
public class DealFragment extends BusinessListFragment {

    @Override
    public String getTitle() {
        return  ContextUtil.getString(R.string.deal);
    }

    @Override
    protected void onCreatePop(PopWidget popWidget) {
        popWidget.add(new CenterPopup.PopItem(0, getString(R.string.buyIn), getResources().getColor(R.color.roundButtonBlue)));
        popWidget.add(new CenterPopup.PopItem(1, getString(R.string.sellOut), getResources().getColor(R.color.roundButtonRed)));
    }

    @Override
    protected void onPopItemClick(PopItem item) {
        super.onPopItemClick(item);
    }
}
