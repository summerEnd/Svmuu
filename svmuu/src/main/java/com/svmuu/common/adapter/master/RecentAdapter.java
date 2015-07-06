package com.svmuu.common.adapter.master;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.svmuu.common.entity.Visitor;
import com.svmuu.ui.activity.live.LiveActivity;

import java.util.List;

public class RecentAdapter extends BaseAdapter<Visitor,MasterAvatarHolder> implements BaseHolder.OnItemListener{
    DisplayImageOptions options;
    public RecentAdapter(@NonNull Context context) {
        super(context);
        options= ImageOptions.getRoundCorner(5);
    }

    public RecentAdapter(Context context, List<Visitor> data) {
        super(context, data);
        options= ImageOptions.getRoundCorner(5);

    }

    @Override
    public MasterAvatarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MasterAvatarHolder ho = new MasterAvatarHolder(getInflater().inflate(R.layout.item_recent_avatar, parent, false));
        ho.setListener(this);
        return ho;
    }

    @Override
    public void onBindViewHolder(MasterAvatarHolder holder, int position) {
        Visitor visitor=getData().get(position);
        holder.tvname.setText(visitor.unick);
        ImageLoader.getInstance().displayImage(visitor.uface,holder.ivavatar,options);
    }

    @Override
    public void onClick(View itemView, int position) {

        Visitor master = getData().get(position);
        Context context = itemView.getContext();
        context.startActivity(new Intent(context, LiveActivity.class)
                .putExtra(LiveActivity.EXTRA_QUANZHU_ID, master.id));
    }
}
