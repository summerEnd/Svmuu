package com.svmuu.common.adapter.live;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.svmuu.R;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.Tests;
import com.svmuu.common.adapter.BaseAdapter;
import com.svmuu.common.adapter.BaseHolder;
import com.svmuu.common.entity.CircleMaster;
import com.svmuu.common.entity.Live;
import com.svmuu.ui.activity.live.LiveActivity;

import java.util.List;


public class LiveAdapter extends BaseAdapter<Live, LiveHolder> implements BaseHolder.OnItemListener {
    DisplayImageOptions options;
    private boolean sortByHot = false;


    public LiveAdapter(Context context) {
        super(context);
        options = ImageOptions.getRoundCorner(5);
    }

    public LiveAdapter(Context context, List<Live> data) {
        super(context, data);
        options = ImageOptions.getRoundCorner(5);
    }

    @Override
    public LiveHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LiveHolder liveHolder = new LiveHolder(getInflater().inflate(R.layout.item_live_grid, viewGroup, false));
        liveHolder.setListener(this);
        return liveHolder;
    }

    public void sortByHot(boolean sortByHot) {
        this.sortByHot = sortByHot;
    }


    @Override
    public void onBindViewHolder(LiveHolder liveHolder, int i) {
        Live live = getData().get(i);
        liveHolder.tvcircleName.setText(live.unick);
        liveHolder.tvcircleNo.setText(getString(R.string.circle_no_s, live.uid));
        if (sortByHot) {
            liveHolder.tvfansNumber.setText( live.hot);
        } else {
            liveHolder.tvfansNumber.setText(getString(R.string.fans_s, live.fans));
        }

        liveHolder.live.setVisibility(live.isOnline?View.VISIBLE:View.INVISIBLE);

        ImageLoader.getInstance().displayImage(live.uface, liveHolder.ivcover, options);
    }

    @Override
    public void onClick(View itemView, int position) {

        getContext().startActivity(new Intent(getContext(), LiveActivity.class)
                .putExtra(LiveActivity.EXTRA_QUANZHU_ID, getData().get(position).uid));
    }
}
