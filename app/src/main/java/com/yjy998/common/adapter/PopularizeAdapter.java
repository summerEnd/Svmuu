package com.yjy998.common.adapter;

import android.content.Context;
import android.view.View;

import com.sp.lib.common.support.adapter.ViewHolderAdapter;
import com.yjy998.R;
import com.yjy998.common.entity.Popularize;

import java.util.List;

public class PopularizeAdapter extends ViewHolderAdapter<Popularize,Object>{

    public PopularizeAdapter(Context context, List<Popularize> data) {
        super(context, data, R.layout.list_item_popularize);
    }

    @Override
    public Object doFindIds(View convertView) {
        ViewHolder holder=new ViewHolder();
        return holder;
    }

    @Override
    public void displayView(View convertView, Object holder, int position) {
        ViewHolder viewHolder= (ViewHolder) holder;

    }

    private class ViewHolder{

    }
}

