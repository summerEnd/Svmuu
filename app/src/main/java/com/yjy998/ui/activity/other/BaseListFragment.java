package com.yjy998.ui.activity.other;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.yjy998.R;

import static android.widget.AdapterView.OnItemClickListener;

public class BaseListFragment extends BaseFragment {
    private ListView listView;
    private OnItemClickListener mOnItemClickListener;
    private ListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        listView = new ListView(getActivity());
        listView.setAdapter(adapter);
        listView.setDivider(new ColorDrawable(getResources().getColor(R.color.lightGray)));
        listView.setDividerHeight(1);
        listView.setOnItemClickListener(mOnItemClickListener);
        return listView;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        if (listView != null) {
            listView.setOnItemClickListener(mOnItemClickListener);
        }
    }

    public ListView getListView() {
        return listView;
    }

    public void setAdapter(ListAdapter adapter) {
        this.adapter = adapter;
        if (listView != null) {
            listView.setAdapter(adapter);
        }
    }
}
