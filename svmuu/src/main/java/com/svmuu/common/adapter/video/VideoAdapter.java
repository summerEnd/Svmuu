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
import com.svmuu.common.adapter.BaseHolder;
import com.svmuu.common.adapter.BaseHolder.OnItemListener;
import com.svmuu.common.entity.Recording;

import java.util.List;


public class VideoAdapter extends BaseAdapter<Recording, VideoHolder> {
    DisplayImageOptions options;
    OnItemListener listener;

    public VideoAdapter(Context context, List<Recording> data) {
        super(context, data);
        options = ImageOptions.getVideoCoverInstance();
    }

    public void setListener(OnItemListener listener) {
        this.listener = listener;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        VideoHolder holder = new VideoHolder(getInflater().inflate(R.layout.video_item, parent, false));
        holder.setListener(listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        Recording recording = getData().get(position);
        holder.subject.setText(recording.subject);
        holder.time.setText(recording.add_time);
        ImageLoader.getInstance().displayImage(recording.cover, holder.cover, options);
    }
}
