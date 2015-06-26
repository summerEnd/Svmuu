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
import com.svmuu.common.adapter.video.VideoAdapter;
import com.svmuu.ui.BaseFragment;


public class VideoFragment extends BaseFragment{
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    protected void initialize() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(new VideoAdapter(getActivity()));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(3,3,3,3);
            }
        });
    }
}
