package com.svmuu.common.adapter.box;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.svmuu.R;
import com.svmuu.common.adapter.BaseAdapter;
import com.svmuu.common.adapter.BaseHolder;
import com.svmuu.common.entity.Box;
import com.svmuu.ui.activity.box.TextBoxActivity;
import com.svmuu.ui.activity.box.VideoBoxActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BoxListAdapter extends BaseAdapter<Box, BoxHolder> implements BaseHolder.OnItemListener {
    public BoxListAdapter(Context context, List<Box> data) {
        super(context, data);
    }

    @Override
    public BoxHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = getInflater().inflate(R.layout.box_list_item, parent, false);
        BoxHolder boxHolder = new BoxHolder(itemView);
        boxHolder.setListener(this);
        return boxHolder;
    }

    @Override
    public void onBindViewHolder(BoxHolder holder, int position) {
        Box box = getData().get(position);
        holder.text.setText(box.name);

        //文字宝盒
        setBoxIcon(holder, box);

        Date date = new Date(Long.decode(box.add_time) * 1000);
        holder.time.setText(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(date));
    }

    //1铁粉2年粉 3公共
    protected void setBoxIcon(BoxHolder holder, Box box) {
        switch (box.free_group) {
            case "1": {
                holder.icon.setImageResource(R.drawable.iron_box_o);
                break;
            }
            case "2": {
                holder.icon.setImageResource(R.drawable.gold_box_o);
                break;
            }
            case "3": {
                holder.icon.setImageResource(R.drawable.com_box_o);
                break;
            }
        }
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
