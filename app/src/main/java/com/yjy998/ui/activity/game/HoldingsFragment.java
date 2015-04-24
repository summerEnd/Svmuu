package com.yjy998.ui.activity.game;


import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;
import com.yjy998.ui.pop.CenterPopup;
import com.yjy998.ui.pop.PayDialog;

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
        popWidget.add(new CenterPopup.PopItem(0, getString(R.string.buyIn), R.drawable.bitmap_blue_button));
        popWidget.add(new CenterPopup.PopItem(1, getString(R.string.sellOut), R.drawable.bitmap_red_button));
    }

    @Override
    protected void onPopItemClick(CenterPopup.PopItem item) {
        switch (item.id) {
            case 0: {
                new PayDialog(getActivity()).show();
                break;
            }
            case 1: {
                break;
            }
        }
    }
}
