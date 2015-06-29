package com.svmuu.common.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseHolder<ET> extends RecyclerView.ViewHolder implements View.OnClickListener{
    OnItemListener listener;
    public BaseHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        initialize();
    }

    protected void initialize() {

    }

    public View findViewById(int id){
        if (itemView==null){
            return null;
        }
        return itemView.findViewById(id);
    }

    @Override
    public void onClick(View v) {

    }

    public void setListener(OnItemListener listener) {
        this.listener = listener;
    }

    public OnItemListener getListener() {
        return listener;
    }

    public interface OnItemListener{
        void onClick(View itemView,int position);
    }
}
