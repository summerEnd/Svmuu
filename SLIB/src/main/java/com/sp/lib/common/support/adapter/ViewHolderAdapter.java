package com.sp.lib.common.support.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public abstract class ViewHolderAdapter<T, H> extends SAdapter<T> {
    int layoutId;
    LayoutInflater inflater;
    protected Context context;

    public ViewHolderAdapter(Context context, List<T> data, int layoutId) {
        super(data);
        this.context = context;
        this.layoutId = layoutId;
        inflater = LayoutInflater.from(context);
    }

    protected final LayoutInflater getLayoutInflater() {
        return inflater;
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        H holder;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(layoutId, null);
            holder = doFindIds(convertView);
        } else {
            holder = (H) convertView.getTag();
        }
        displayView(convertView, holder, position);
        convertView.setTag(holder);
        return convertView;
    }


    /**
     * 创建一个ViewHolder，这个对象会作为convertView的tag.
     * 你应该在这里创建一个属于你自己的ViewHolder,并将需要的控件调用convertView的findViewById()方法赋值给ViewHolder中的变量，;
     *
     * @param convertView 视图
     */
    public abstract H doFindIds(View convertView);

    /**
     * @param holder   对应当前item的holder
     * @param position 对应当前item位置
     */
    public abstract void displayView(View convertView, H holder, int position);

    public Context getContext() {
        return context;
    }

}
