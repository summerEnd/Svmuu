package com.svmuu.common.adapter.video;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.svmuu.R;
import com.svmuu.common.adapter.BaseHolder;

public class VideoHolder extends BaseHolder {
    public ImageView cover;
    public TextView subject;
    public TextView time;

    public VideoHolder(View itemView) {
        super(itemView);
    }

    protected void initialize() {

        cover = (ImageView) findViewById(R.id.cover);
        subject = (TextView) findViewById(R.id.subject);
        time = (TextView) findViewById(R.id.time);
    }
}
