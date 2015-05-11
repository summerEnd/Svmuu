package com.yjy998.ui.activity.main.my.business;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.sp.lib.widget.list.refresh.PullToRefreshListView;
import com.yjy998.R;
import com.yjy998.common.entity.ContractDetail;
import com.yjy998.ui.activity.base.BaseFragment;
import com.yjy998.ui.pop.CenterPopup;

import static com.yjy998.ui.activity.main.my.business.capital.BuySellFragment.ContractObserver;
import static com.yjy998.ui.pop.CenterPopup.*;

public class BusinessListFragment extends BaseFragment implements AdapterView.OnItemLongClickListener, Listener {

    View layout;
    PullToRefreshListView mRefreshList;
    CenterPopup mCenterPopup;
    private ListAdapter adapter;
    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //防止多次初始化，因为ViewPager会销毁没有展示的View，导致重新创建视图。
        if (layout != null) {
            ((ViewGroup) layout.getParent()).removeView(layout);
        } else {
            layout = inflater.inflate(R.layout.fragment_cancellation_etrust, container, false);
            mRefreshList = (PullToRefreshListView) layout.findViewById(R.id.list);
            mRefreshList.setPullRefreshEnabled(false);
            ListView refreshableView = mRefreshList.getRefreshableView();
            refreshableView.setAdapter(adapter);
            refreshableView.setOnItemLongClickListener(this);
            mRefreshList.setHasMoreData(true);
            mRefreshList.setPullLoadEnabled(true);
        }
        return layout;
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

    public ListAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ListAdapter adapter) {
        this.adapter = adapter;
        if (mRefreshList != null) {
            mRefreshList.getRefreshableView().setAdapter(adapter);
        }

    }
}
