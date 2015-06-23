package com.svmuu.common.adapter.master;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.svmuu.R;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.Tests;
import com.svmuu.common.adapter.BaseAdapter;

public class MasterAvatar extends BaseAdapter<MasterAvatarHolder>{
    LayoutInflater inflater;
    DisplayImageOptions options;

    public MasterAvatar(@NonNull Context context) {
        super(context);
        inflater=LayoutInflater.from(context);
        options= ImageOptions.getRoundCorner(5);
    }

    @Override
    public MasterAvatarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MasterAvatarHolder(inflater.inflate(R.layout.item_recent_avatar,parent,false));
    }

    @Override
    public void onBindViewHolder(MasterAvatarHolder holder, int position) {
        ImageLoader.getInstance().displayImage(Tests.IMAGE,holder.ivavatar,options);
    }

    @Override
    public int getItemCount() {
        return 10;
    }


}
