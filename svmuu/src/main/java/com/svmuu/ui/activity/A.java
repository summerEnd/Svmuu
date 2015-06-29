package com.svmuu.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.sp.lib.widget.list.refresh.PullToRefreshRecyclerView;
import com.svmuu.R;
import com.svmuu.common.adapter.live.LiveAdapter;

public class A extends ActionBarActivity {

    private PullToRefreshRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialize() {

        recyclerView = (PullToRefreshRecyclerView) findViewById(R.id.recyclerView);
        RecyclerView refreshableView = recyclerView.getRefreshableView();
        refreshableView.setLayoutManager(new GridLayoutManager(this, 4));
        refreshableView.setAdapter(new LiveAdapter(this));
        recyclerView.setPullLoadEnabled(true);
        recyclerView.setPullRefreshEnabled(true);
    }
}