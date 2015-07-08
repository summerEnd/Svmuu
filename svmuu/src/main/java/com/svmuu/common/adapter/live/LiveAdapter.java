package com.svmuu.common.adapter.live;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.svmuu.R;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.adapter.BaseAdapter;
import com.svmuu.common.adapter.BaseHolder;
import com.svmuu.common.entity.Live;
import com.svmuu.ui.activity.live.LiveActivity;

import java.util.List;


public class LiveAdapter extends BaseAdapter<Live, LiveHolder> implements BaseHolder.OnItemListener {
    DisplayImageOptions options;
    private boolean showIsLive = false;


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

    public void showIsLive(boolean sortByHot) {
        this.showIsLive = sortByHot;
    }

    @Override
    public void onBindViewHolder(LiveHolder liveHolder, int i) {
        Live live = getData().get(i);
        liveHolder.tvcircleName.setText(live.unick);
        liveHolder.tvcircleNo.setText(getString(R.string.circle_no_s, live.uid));
        if (live.live) {
            liveHolder.live.setBackgroundColor(0xffe5376b);
        } else {
            liveHolder.live.setBackgroundColor(0xffcccccc);
        }
        liveHolder.tvfansNumber.setText(live.hot);


        ImageLoader.getInstance().displayImage(live.uface, liveHolder.ivcover, options);
    }

    @Override
    public void onClick(View itemView, int position) {

        getContext().startActivity(new Intent(getContext(), LiveActivity.class)
                .putExtra(LiveActivity.EXTRA_QUANZHU_ID, getData().get(position).uid));
    }
}
