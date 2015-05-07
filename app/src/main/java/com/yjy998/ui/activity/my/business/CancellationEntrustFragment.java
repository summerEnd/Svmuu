package com.yjy998.ui.activity.my.business;


import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;

import static com.yjy998.ui.pop.CenterPopup.PopItem;
import static com.yjy998.ui.pop.CenterPopup.PopWidget;

/**
 * 委托/撤单
 */
public class CancellationEntrustFragment extends BusinessListFragment {


    @Override
    public String getTitle() {
        return ContextUtil.getString(R.string.cancellationEntrust);
    }

    @Override
    protected void onCreatePop(PopWidget popWidget) {
        popWidget.add(new PopItem(1,getString(R.string.cancelOrder), getResources().getColor(R.color.roundButtonBlue)));
        popWidget.add(new PopItem(2,getString(R.string.cancelAndRebuy),  getResources().getColor(R.color.roundButtonRed)));
    }

    @Override
    protected void onPopItemClick(PopItem item) {
        ContextUtil.toast_debug(item.text);
    }
}
