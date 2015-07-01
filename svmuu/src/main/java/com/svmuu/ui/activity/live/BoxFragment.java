package com.svmuu.ui.activity.live;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.svmuu.R;
import com.svmuu.common.adapter.box.BoxAdapter;
import com.svmuu.common.entity.Box;
import com.svmuu.ui.BaseFragment;

import java.util.ArrayList;

public class BoxFragment extends BaseFragment{
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_box,container,false);
    }

    @Override
    protected void initialize() {
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));
        BoxAdapter adapter = new BoxAdapter(getActivity(),new ArrayList<Box>());
        adapter.setViewType(BoxAdapter.VIEW_GRID);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(6,6,6,6);
            }
        });
    }
}
