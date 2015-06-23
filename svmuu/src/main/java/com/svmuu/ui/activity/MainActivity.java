package com.svmuu.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sp.lib.common.support.update.Downloader;
import com.sp.lib.widget.list.LinearListView;
import com.svmuu.R;
import com.svmuu.common.Constant;
import com.svmuu.common.adapter.master.MasterAvatar;
import com.svmuu.common.adapter.other.RecommendAdapter;
import com.svmuu.common.entity.CircleMaster;
import com.svmuu.ui.pop.YAlertDialog;
import com.svmuu.ui.pop.YAlertDialogTwoButton;
import com.svmuu.ui.widget.CustomSearchView;

import java.util.ArrayList;

public class MainActivity extends MenuActivity implements CustomSearchView.Callback {


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
        list.setAdapter(new RecommendAdapter(this, new ArrayList<CircleMaster>()));

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recentContainer.setLayoutManager(manager);
        recentContainer.setAdapter(new MasterAvatar(this));
        searchView.setCallback(this);

        findViewById(R.id.liveRoom).setOnClickListener(this);
        findViewById(R.id.realContest).setOnClickListener(this);
        findViewById(R.id.stockSchool).setOnClickListener(this);
        findViewById(R.id.center).setOnClickListener(this);
    }

    @Override
    public void onSearch(String key) {

    }

    @Override
    public void onJump() {
        startActivity(new Intent(this, SearchActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.liveRoom: {
                break;
            }
            case R.id.realContest: {
                try {
                    Intent intent = new Intent(Constant.YJY_EXPORT_Game);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                } catch (Exception e) {
                    //易交易没有安装
                    YAlertDialog dialog = new YAlertDialog(this);
                    dialog.setMessage(getString(R.string.yjy));

                    dialog.setButton(getString(R.string.download_now), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    dialog.show();
                }
                break;
            }
            case R.id.stockSchool: {
                break;
            }
            case R.id.center: {
                break;
            }
        }
        super.onClick(v);
    }
}
