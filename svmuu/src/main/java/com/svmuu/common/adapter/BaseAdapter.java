package com.svmuu.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by Lincoln on 15/6/18.
 */
public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{
    private Context context;
    private LayoutInflater inflater;
    public BaseAdapter(@NonNull Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }


    public LayoutInflater getInflater(){
        if (inflater==null){
            inflater=LayoutInflater.from(context);
        }
        return inflater;
    }


}
