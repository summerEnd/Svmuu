package com.sp.lib.support.adapter;

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
    private int     size;
    ArrayList list = new ArrayList();

    public EmptyAdapter(Context context, int size) {
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

    private EmptyAdapter(List data) {
        super(data);
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View createView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(android.R.layout.simple_list_item_1, null);
    }

    @Override
    public Object createHolder(View convertView) {
        Holder holder = new Holder();
        holder.tv = (TextView) convertView;
        return holder;
    }

    @Override
    public void setData(View v,Object holder, Object data) {
        Holder viewHolder = (Holder) holder;
        viewHolder.tv.setText(String.valueOf(data));
    }

    private class Holder {
        TextView tv;
    }
}
