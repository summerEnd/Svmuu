package com.svmuu.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

/**
 * Created by Lincoln on 15/6/18.
 */
public abstract class BaseAdapter<ET,VH extends BaseHolder> extends RecyclerView.Adapter<VH>{
    private Context context;
    private LayoutInflater inflater;
    private List<ET> data;

    public BaseAdapter(Context context, List<ET> data) {
        this.context = context;
        this.data = data;
    }



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

    public List<ET> getData() {
        return data;
    }

    @Override
    public int getItemCount() {
        if (data!=null){
            return data.size();
        }
        return 0;
    }

    public String getString(int resId){
        return getContext().getString(resId);
    }

    public String getString(int resId,Object...args){
        return getContext().getString(resId,args);
    }

}
