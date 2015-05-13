package com.yjy998.ui.activity.base;

import android.database.DataSetObserver;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sp.lib.widget.list.refresh.PullToRefreshListView;
import com.yjy998.R;

import static android.widget.AdapterView.OnItemClickListener;

public class BaseListFragment extends BaseFragment {
    private PullToRefreshListView refreshList;
    private OnItemClickListener mOnItemClickListener;
    private ListAdapter adapter;
    private View emptyView;
    private ListObserver observer;
    private ViewGroup parent;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        emptyView = inflater.inflate(R.layout.empty_layout, null);
        parent = (ViewGroup) inflater.inflate(R.layout.fragment_refershable_list, null);
        refreshList = (PullToRefreshListView) parent.findViewById(R.id.list);
        refreshList.setPullRefreshEnabled(false);
        refreshList.setPullLoadEnabled(false);
        refreshList.getRefreshableView().setAdapter(adapter);
        refreshList.getRefreshableView().setDivider(new ColorDrawable(getResources().getColor(R.color.lightGray)));
        refreshList.getRefreshableView().setDividerHeight(1);
        refreshList.getRefreshableView().setOnItemClickListener(mOnItemClickListener);

        registerObserver();

        String title = getTitle();
        if (title != null) {
            TextView emptyText = (TextView) emptyView.findViewById(R.id.noDataText);
            emptyText.setText(getString(R.string.no_s_data, title));
        }

        return parent;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateEmptyStatus();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        if (refreshList != null) {
            refreshList.getRefreshableView().setOnItemClickListener(mOnItemClickListener);
        }
    }

    public ListView getListView() {
        return refreshList.getRefreshableView();
    }

    public void setAdapter(ListAdapter adapter) {
        this.adapter = adapter;
        if (refreshList != null) {
            refreshList.getRefreshableView().setAdapter(adapter);
            registerObserver();
        }
    }

    public ListAdapter getAdapter() {
        return adapter;
    }

    private void registerObserver() {
        try {
            if (adapter != null) {
                if (observer == null) {
                    observer = new ListObserver();
                } else {
                    unRegisterObserver();
                }
                adapter.registerDataSetObserver(observer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void unRegisterObserver() {
        try {
            if (adapter != null) {
                adapter.unregisterDataSetObserver(observer);
            }
        } catch (Exception e) {
        }
    }

    void updateEmptyStatus() {
        parent.removeAllViews();
        if (adapter != null && adapter.getCount() != 0) {
            parent.addView(refreshList);
        } else {
            parent.addView(emptyView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterObserver();
    }

    private class ListObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            updateEmptyStatus();
        }
    }
}
