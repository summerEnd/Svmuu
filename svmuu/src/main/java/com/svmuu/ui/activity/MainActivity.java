package com.svmuu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.common.util.SLog;
import com.sp.lib.widget.list.LinearListView;
import com.svmuu.AppDelegate;
import com.svmuu.R;
import com.svmuu.common.config.Constant;
import com.svmuu.common.adapter.master.RecentAdapter;
import com.svmuu.common.adapter.other.RecommendAdapter;
import com.svmuu.common.entity.CircleMaster;
import com.svmuu.common.entity.Visitor;
import com.svmuu.common.http.HttpHandler;
import com.svmuu.common.http.HttpManager;
import com.svmuu.common.http.Response;
import com.svmuu.ui.pop.YAlertDialog;
import com.svmuu.ui.widget.CustomSearchView;

import org.apache.http.Header;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends MenuActivity implements CustomSearchView.Callback {


    private CustomSearchView searchView;
    private RecyclerView recentContainer;
    private LinearListView list;
    _DATA mData;
    private RecentAdapter recentAdapter;
    private RecommendAdapter recommendAdapter;

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
        recommendAdapter = new RecommendAdapter(this, new ArrayList<CircleMaster>());
        list.setAdapter(recommendAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recentContainer.setLayoutManager(manager);
        recentAdapter = new RecentAdapter(this,new ArrayList<Visitor>());
        recentContainer.setAdapter(recentAdapter);

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
                startActivity(new Intent(this,MyCircleActivity.class));
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
                YAlertDialog.showNoSuchFunction(this);
                break;
            }
            case R.id.center: {
                open();
                break;
            }
        }
        super.onClick(v);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppDelegate.getInstance().isLogin()){
            getRecent();
        }
    }

    public void getRecent(){
        SRequest request=new SRequest("/moblieapi/recent");
        HttpManager.getInstance().post(this,request, new HttpHandler() {
            @Override
            public void onResultOk(int statusCOde, Header[] headers, Response response) throws JSONException{
                SLog.debug(response);
                mData = JsonUtil.get(response.data,_DATA.class);
                recommendAdapter.getData().addAll(mData.quanzhu);
                recentAdapter.getData().addAll(mData.visitor);
                recommendAdapter.notifyDataSetChanged();
                recentAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onUserChanged() {
        super.onUserChanged();
        getRecent();
    }

    private class _DATA{
        ArrayList<Visitor> visitor;
        ArrayList<CircleMaster> quanzhu;
    }

}
