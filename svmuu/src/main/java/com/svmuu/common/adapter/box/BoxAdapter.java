package com.svmuu.common.adapter.box;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.svmuu.R;
import com.svmuu.common.adapter.BaseAdapter;

public class BoxAdapter extends BaseAdapter<BoxHolder> {

    public BoxAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public BoxHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoxHolder(getInflater().inflate(R.layout.box_item,parent,false));
    }

    @Override
    public void onBindViewHolder(BoxHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
