package com.yjy998.adapter;

import android.content.Context;
import android.view.View;

import com.sp.lib.common.support.adapter.ViewHolderAdapter;
import com.yjy998.entity.Hold;

import java.util.List;


public class HoldingsAdapter extends ViewHolderAdapter<Hold, Object> {


    public HoldingsAdapter(Context context, List<Hold> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public Object doFindIds(View convertView) {
        ViewHolder holder = new ViewHolder();
        return holder;
    }

    @Override
    public void displayView(View convertView, Object holder, int position) {

    }

    private class ViewHolder {
    }
}
