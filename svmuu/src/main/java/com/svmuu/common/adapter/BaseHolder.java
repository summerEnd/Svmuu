package com.svmuu.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
}
