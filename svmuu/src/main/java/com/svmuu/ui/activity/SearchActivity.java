package com.svmuu.ui.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sp.lib.common.support.cache.CacheManager;
import com.svmuu.R;
import com.svmuu.common.adapter.DividerDecoration;
import com.svmuu.common.adapter.search.SearchAdapter;
import com.svmuu.common.entity.History;
import com.svmuu.ui.BaseActivity;
import com.svmuu.ui.widget.CustomSearchView;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity implements CustomSearchView.Callback {


    public static final String SEARCH_HISTORIES = "search_histories";
    private CustomSearchView searchView;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    ArrayList<History> histories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initialize();
    }


    @SuppressWarnings("unchecked")
    private void initialize() {
        try {
            histories = (ArrayList<History>) CacheManager.getInstance().read(SEARCH_HISTORIES);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (histories == null) {
                histories = new ArrayList<>();
            }
        }

        searchView = (CustomSearchView) findViewById(R.id.searchView);
        findViewById(R.id.cancel_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        searchView.setCallback(this);
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        adapter = new SearchAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerDecoration(this));
        adapter.showHistory(histories);
    }

    @Override
    public void onSearch(String key) {
        History history = new History();
        history.name = key;
        if (!histories.contains(history)) {
            histories.add(history);
            CacheManager.getInstance().write(SEARCH_HISTORIES, histories);
        }
        adapter.showResult(null);

    }

    @Override
    public void onJump() {

    }
}