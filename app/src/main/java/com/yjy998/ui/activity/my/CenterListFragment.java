package com.yjy998.ui.activity.my;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.widget.list.refresh.PullToRefreshListView;
import com.yjy998.R;
import com.yjy998.adapter.HoldingsAdapter;
import com.yjy998.entity.Hold;
import com.yjy998.ui.activity.BaseFragment;
import com.yjy998.ui.pop.CenterPopup;

import java.util.ArrayList;
import java.util.List;

import static com.yjy998.ui.pop.CenterPopup.*;

public class CenterListFragment extends BaseFragment implements AdapterView.OnItemClickListener, Listener {

    View layout;
    PullToRefreshListView mRefreshList;
    CenterPopup mCenterPopup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_cancellation_etrust, container, false);
        mRefreshList = (PullToRefreshListView) layout.findViewById(R.id.list);
        ListView refreshableView = mRefreshList.getRefreshableView();
        refreshableView.setAdapter(new HoldingsAdapter(getActivity(), getData()));
        refreshableView.setOnItemClickListener(this);
        mRefreshList.setHasMoreData(true);
        mRefreshList.setPullLoadEnabled(true);

        return layout;
    }

    private List<Hold> getData() {
        List<Hold> data = new ArrayList<Hold>();
        for (int i = 0; i < 20; i++) {
            Hold hold = new Hold();
            data.add(hold);
        }
        return data;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mCenterPopup == null) {
            mCenterPopup = new CenterPopup(getActivity());
            PopWidget widget = new PopWidget();
            onCreatePop(widget);
            //添加取消按钮
            widget.add(new PopItem(R.string.cancel, getString(R.string.cancel), R.drawable.bitmap_gray_button));
            mCenterPopup.setPopWidget(widget);
            mCenterPopup.setAnimationStyle(R.style.centerPopAnimation);
            mCenterPopup.setListener(this);
        }
        mCenterPopup.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    protected void onCreatePop(PopWidget popWidget) {

    }

    protected void onPopItemClick(PopItem item) {
    }

    @Override
    public final void onClick(PopItem item) {
        if (item.id == R.string.cancel) {
            mCenterPopup.dismiss();
        }else{
            onPopItemClick(item);
        }
    }
}
