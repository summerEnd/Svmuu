package com.svmuu.common.adapter.box;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.svmuu.R;
import com.svmuu.common.entity.Box;

import java.util.List;

public class BoxGridAdapter extends BoxListAdapter {
    public BoxGridAdapter(Context context, List<Box> data) {
        super(context, data);
    }

    @Override
    public BoxHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = getInflater().inflate(R.layout.box_grid_item, parent, false);
        BoxHolder boxHolder = new BoxHolder(itemView);
        boxHolder.setListener(this);
        return boxHolder;
    }

    @Override
    public void onBindViewHolder(BoxHolder holder, int position) {
        Box box = getData().get(position);
        holder.text.setText(box.name);
        //1铁粉2年粉 3公共
        setBoxIcon(holder, box);
    }
}
