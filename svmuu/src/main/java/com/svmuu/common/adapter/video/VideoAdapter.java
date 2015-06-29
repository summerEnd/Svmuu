package com.svmuu.common.adapter.video;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.svmuu.R;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.Tests;
import com.svmuu.common.adapter.BaseAdapter;
import com.svmuu.common.entity.Recording;


public class VideoAdapter extends BaseAdapter<Recording,VideoHolder>{
    LayoutInflater inflater;
    DisplayImageOptions options;
    public VideoAdapter(@NonNull Context context) {
        super(context);
        inflater=getInflater();
        options= ImageOptions.getRoundCorner(5);
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoHolder(inflater.inflate(R.layout.video_item,parent,false));
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        ImageLoader.getInstance().displayImage(Tests.IMAGE_02,holder.cover,options);
    }

    @Override
    public int getItemCount() {
        return 20;
    }
}
