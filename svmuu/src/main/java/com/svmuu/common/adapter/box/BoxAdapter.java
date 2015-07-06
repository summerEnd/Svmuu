package com.svmuu.common.adapter.box;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.svmuu.R;
import com.svmuu.common.adapter.BaseAdapter;
import com.svmuu.common.adapter.BaseHolder;
import com.svmuu.common.entity.Box;
import com.svmuu.ui.activity.box.VideoBoxActivity;
import com.svmuu.ui.activity.box.TextBoxActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BoxAdapter extends BaseAdapter<Box, BoxHolder> implements BaseHolder.OnItemListener {

    public static final int VIEW_LIST = 0;
    public static final int VIEW_GRID = 1;

    int viewType = VIEW_LIST;


    public BoxAdapter(Context context, List<Box> data) {
        super(context, data);
    }

    @Override
    public BoxHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;


        if (viewType == VIEW_LIST) {
            itemView = getInflater().inflate(R.layout.box_list_item, parent, false);
        } else {
            itemView = getInflater().inflate(R.layout.box_grid_item, parent, false);
        }
        BoxHolder holder = new BoxHolder(itemView);
        holder.setListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(BoxHolder holder, int position) {
        Box box = getData().get(position);
        holder.text.setText(box.name);
        //1铁粉2年粉 3公共
        switch (box.free_group) {
            case "1": {
                holder.icon.setImageResource(R.drawable.iron_box);
                break;
            }
            case "2": {
                holder.icon.setImageResource(R.drawable.gold_box);
                break;
            }
            case "3": {
                holder.icon.setImageResource(R.drawable.com_box);
                break;
            }
        }

        switch (getItemViewType(position)) {
            case VIEW_LIST: {
                Date date = new Date(Long.decode(box.add_time) * 1000);
                holder.time.setText(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(date));
                break;
            }
            case VIEW_GRID: {
                break;
            }
        }

    }

    @Override
    public int getItemViewType(int position) {

        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public void onClick(View itemView, int position) {
        Context context = getContext();


        Box box = getData().get(position);
        if ("2".equals(box.type)) {
            //视频宝盒
            context.startActivity(new Intent(context, VideoBoxActivity.class)
                    .putExtra(VideoBoxActivity.EXTRA_ID, box.id));

        } else {
            //文字宝盒
            context.startActivity(new Intent(context, TextBoxActivity.class)
                    .putExtra(TextBoxActivity.EXTRA_ID, box.id));

        }

    }
}
