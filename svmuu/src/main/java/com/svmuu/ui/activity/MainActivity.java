package com.svmuu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.sp.lib.widget.list.LinearListView;
import com.svmuu.R;
import com.svmuu.common.adapter.master.MasterAvatar;
import com.svmuu.common.adapter.other.RecommendAdapter;
import com.svmuu.common.entity.CircleMaster;
import com.svmuu.ui.widget.CustomSearchView;

import java.util.ArrayList;

public class MainActivity extends MenuActivity implements CustomSearchView.Callback{


    private CustomSearchView searchView;
    private RecyclerView recentContainer;
    private LinearListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initialize();
    }

    private void initialize() {

        searchView = (CustomSearchView) findViewById(R.id.searchView);
        recentContainer = (RecyclerView) findViewById(R.id.recentContainer);
        list = (LinearListView) findViewById(R.id.list);
        list.setAdapter(new RecommendAdapter(this,new ArrayList<CircleMaster>()));

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recentContainer.setLayoutManager(manager);
        recentContainer.setAdapter(new MasterAvatar(this));
        searchView.setCallback(this);
    }

    @Override
    public void onSearch(String key) {

    }

    @Override
    public void onJump() {
        startActivity(new Intent(this,SearchActivity.class));
    }
}
