package com.svmuu.common.adapter.live;

import android.content.Context;
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


public class LiveAdapter extends BaseAdapter<LiveHolder> {
    LayoutInflater inflater;
    DisplayImageOptions options;

    public LiveAdapter(Context context) {
        super(context);
        options= ImageOptions.getRoundCorner(5);
        inflater=getInflater();
    }

    @Override
    public LiveHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new LiveHolder(inflater.inflate(R.layout.item_live_grid,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(LiveHolder liveHolder, int i) {
        ImageLoader.getInstance().displayImage(Tests.IMAGE,liveHolder.ivcover,options);
    }

    @Override
    public int getItemCount() {
        return 10;
    }


}
