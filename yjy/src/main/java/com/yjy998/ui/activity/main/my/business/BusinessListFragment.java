package com.yjy998.ui.activity.main.my.business;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.yjy998.R;
import com.yjy998.common.entity.ContractDetail;
import com.yjy998.ui.activity.base.BaseListFragment;
import com.yjy998.ui.pop.CenterPopup;

import static com.yjy998.ui.activity.main.my.business.capital.TradeFragment.ContractObserver;
import static com.yjy998.ui.pop.CenterPopup.*;

public class BusinessListFragment extends BaseListFragment implements AdapterView.OnItemLongClickListener, Listener {

    CenterPopup mCenterPopup;
    private int position;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnItemLongClickListener(this);
    }

    /**
     * 获取共享的合约
     */
    ContractDetail getSharedContract() {
        if (getActivity() instanceof ContractObserver) {
            return ((ContractObserver) getActivity()).getSharedContract();
        }
        return null;
    }


    //回调方法
    protected void onCreatePop(PopWidget popWidget) {

    }

    //回调方法
    protected void onPopItemClick(PopItem item) {
    }

    @Override
    public final void onClick(PopItem item) {
        if (item.id == R.string.cancel) {
            mCenterPopup.dismiss();
        } else {
            onPopItemClick(item);
            mCenterPopup.dismiss();
        }
    }

    /**
     * 获取列表中选中的位置
     */
    public int getSelectedPosition() {
        return position;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;
        if (mCenterPopup == null) {
            PopWidget widget = new PopWidget();
            onCreatePop(widget);
            //添加取消按钮
            if (widget.getChildrenCount() != 0) {
                widget.add(new PopItem(R.string.cancel, getString(R.string.cancel), 0xff949494));
            } else {
                return false;
            }

            mCenterPopup = new CenterPopup(getActivity());
            mCenterPopup.setPopWidget(widget);
            mCenterPopup.setAnimationStyle(R.style.businessPopAnimation);
            mCenterPopup.setListener(this);
        }
        mCenterPopup.show(view);
        return true;
    }
}
