package com.svmuu.common.adapter.master;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.svmuu.R;
import com.svmuu.common.adapter.BaseAdapter;

public class MasterAvatar extends BaseAdapter<MasterAvatarHolder>{
    LayoutInflater inflater;
    public MasterAvatar(@NonNull Context context) {
        super(context);
        inflater=LayoutInflater.from(context);
    }

    @Override
    public MasterAvatarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MasterAvatarHolder(inflater.inflate(R.layout.item_recent_avatar,parent,false));
    }

    @Override
    public void onBindViewHolder(MasterAvatarHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
