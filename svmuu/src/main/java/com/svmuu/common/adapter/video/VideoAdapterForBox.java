package com.svmuu.common.adapter.video;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.svmuu.R;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.adapter.BaseAdapter;
import com.svmuu.common.adapter.BaseHolder.OnItemListener;
import com.svmuu.common.entity.BoxVideoDetail;
import com.svmuu.common.entity.Recording;
import com.svmuu.ui.activity.box.BoxDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class VideoAdapterForBox extends BaseAdapter<BoxVideoDetail, VideoHolder> implements OnItemListener {
    DisplayImageOptions options;
    SimpleDateFormat timeFormat;

    public VideoAdapterForBox(Context context, List<BoxVideoDetail> data) {
        super(context, data);
        options = ImageOptions.getRoundCorner(5);
        timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
    }


    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        VideoHolder holder = new VideoHolder(getInflater().inflate(R.layout.video_item, parent, false));
        holder.setListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        BoxVideoDetail detail = getData().get(position);
        holder.subject.setText(detail.subject);
        if (!detail.add_time.contains("-")) {
            detail.add_time = timeFormat.format(new Date(Long.decode(detail.add_time) * 1000));
        }

        holder.time.setText(detail.add_time);
        ImageLoader.getInstance().displayImage(detail.cover, holder.cover, options);
    }

    @Override
    public void onClick(View itemView, int position) {
        BoxVideoDetail detail=getData().get(position);
        getContext().startActivity(new Intent(getContext(), BoxDetailActivity.class)
            .putExtra(BoxDetailActivity.EXTRA_SUBJECT,detail.subject)
            .putExtra(BoxDetailActivity.EXTRA_TOKEN,detail.pwd)
            .putExtra(BoxDetailActivity.EXTRA_ID,detail.id)
        );
    }
}
