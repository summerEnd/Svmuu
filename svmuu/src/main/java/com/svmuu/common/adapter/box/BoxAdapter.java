package com.svmuu.common.adapter.box;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.svmuu.R;
import com.svmuu.common.adapter.BaseAdapter;
import com.svmuu.common.entity.Box;

public class BoxAdapter extends BaseAdapter<Box, BoxHolder> {

    public static final int VIEW_LIST = 0;
    public static final int VIEW_GRID = 1;

    int viewType = VIEW_LIST;

    public BoxAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public BoxHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_LIST: {
                return new BoxHolder(getInflater().inflate(R.layout.box_list_item, parent, false));
            }
            case VIEW_GRID: {
                return new BoxHolder(getInflater().inflate(R.layout.box_grid_item, parent, false));
            }
        }

        return null;
    }

    @Override
    public void onBindViewHolder(BoxHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_LIST: {
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
    public int getItemCount() {
        return 10;
    }
}
