package com.sp.lib.common.support.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class SAdapter<T> extends BaseAdapter {

    protected List<T> mData;
    private int count = -1;

    public SAdapter(List<T> data) {
        mData = data;
    }

    @Override
    public final int getCount() {
        if (count >= 0) {
            return count;
        }

        if (mData == null) {
            return 0;
        }

        return mData.size();
    }

    public List<T> getData() {
        return mData;
    }

    @Override
    public final T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public final long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    /**
     * 设置一个测试的数量,如果小于0就代表不适用测试数量。
     */
    public final void setTestCount(int count) {
        this.count = count;
    }
}
