package com.sp.lib.common.support.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个空的Adapter测试listView用的
 */
public class EmptyAdapter extends ViewHolderAdapter {
    public Context context;
    private int size;
    static ArrayList list = new ArrayList();

    public EmptyAdapter(Context context, int size) {
        super(context, list, android.R.layout.simple_list_item_1);
        this.context = context;
        setSize(size);
    }

    public void setSize(int size) {
        this.size = size;
        list.clear();
        for (int i = 0; i < size; i++) {
            list.add("holder__" + i);
        }
        notifyDataSetChanged();
    }

    public int getSize() {
        return size;
    }


    @Override
    public Object doFindIds(View convertView) {
        Holder holder = new Holder();
        holder.tv = (TextView) convertView;
        return holder;
    }

    @Override
    public void displayView(View convertView, Object holder, int position) {
        Holder viewHolder = (Holder) holder;
        viewHolder.tv.setText(String.valueOf(getItem(position)));
    }


    private class Holder {
        TextView tv;
    }
}
