package com.svmuu.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.common.util.SLog;
import com.sp.lib.widget.list.LinearListView;
import com.sp.lib.widget.list.refresh.PullToRefreshBase;
import com.sp.lib.widget.list.refresh.PullToRefreshScrollView;
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
import com.svmuu.ui.activity.live.LiveActivity;
import com.svmuu.ui.pop.YAlertDialog;
import com.svmuu.ui.widget.CustomSearchView;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MenuActivity implements CustomSearchView.Callback {


    private CustomSearchView searchView;
    private RecyclerView recentContainer;
    private LinearListView list;
    _DATA mData;
    private RecentAdapter recentAdapter;
    private RecommendAdapter recommendAdapter;
    private PullToRefreshScrollView refreshScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        refreshScrollView = new PullToRefreshScrollView(this);
        refreshScrollView.setPullRefreshEnabled(true);
        ScrollView scrollView = refreshScrollView.getRefreshableView();
        //把页面加入到下拉刷新中
        getLayoutInflater().inflate(R.layout.activity_main2, scrollView, true);
        refreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getRecent();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
        setContentView(refreshScrollView);
        initialize();
    }

    private void initialize() {

        searchView = (CustomSearchView) findViewById(R.id.searchView);
        recentContainer = (RecyclerView) findViewById(R.id.recentContainer);
        list = (LinearListView) findViewById(R.id.list);
        recommendAdapter = new RecommendAdapter(this, new ArrayList<CircleMaster>());
        list.setAdapter(recommendAdapter);
        list.setOnItemClick(new LinearListView.OnItemClick() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                CircleMaster master = recommendAdapter.getData().get(position);
                startActivity(new Intent(MainActivity.this, LiveActivity.class)
                        .putExtra(LiveActivity.EXTRA_QUANZHU_ID, master.uid));
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recentContainer.setLayoutManager(manager);
        recentAdapter = new RecentAdapter(this, new ArrayList<Visitor>());
        recentContainer.setAdapter(recentAdapter);

        searchView.setCallback(this);

        findViewById(R.id.liveRoom).setOnClickListener(this);
        findViewById(R.id.realContest).setOnClickListener(this);
        findViewById(R.id.stockSchool).setOnClickListener(this);
        findViewById(R.id.center).setOnClickListener(this);

        getRecent();
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
                startActivity(new Intent(this, MyCircleActivity.class));
                break;
            }
            case R.id.realContest: {
                try {
                    Intent intent = new Intent(Constant.YJY_EXPORT_Game);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                } catch (Exception e) {
                    //易交易没有安装
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setMessage(getString(R.string.yjy));

                    builder.setPositiveButton(R.string.download_now, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri uri = Uri.parse("http://app.mi.com/download/99749?ref=search");
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });

                    builder.show();
                }
                break;
            }
            case R.id.stockSchool: {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.warn)
                        .setMessage(R.string.function_not_open)
                        .setPositiveButton(R.string.i_know, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
                break;
            }
            case R.id.center: {
                open();
                break;
            }
        }
        super.onClick(v);
    }

    public void getRecent() {
        SRequest request = new SRequest("recent");
        HttpManager.getInstance().postMobileApi(this, request, new HttpHandler() {
            @Override
            public void onResultOk(int statusCOde, Header[] headers, Response response) throws JSONException {
                SLog.debug(response);
                JSONObject data = new JSONObject(response.data);
//                mData = JsonUtil.get(response.data, _DATA.class);
                mData = new _DATA();

                mData.quanzhu = JsonUtil.getArray(data.optJSONArray("quanzhu"), CircleMaster.class);
                mData.visitor = JsonUtil.getArray(data.optJSONArray("visitor"), Visitor.class);
                List<CircleMaster> recommends = recommendAdapter.getData();
                recommends.clear();
                if (mData.quanzhu != null) {
                    recommends.addAll(mData.quanzhu);
                }

                List<Visitor> visitors = recentAdapter.getData();
                visitors.clear();
                if (mData.visitor != null) {
                    visitors.addAll(mData.visitor);
                }

                recommendAdapter.notifyDataSetChanged();
                recentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                refreshScrollView.onPullDownRefreshComplete();
            }
        });
    }

    @Override
    public void onUserChanged() {
        if (AppDelegate.getInstance().isLogin()) {
            getRecent();
        } else {
            recommendAdapter.getData().clear();
            recommendAdapter.notifyDataSetChanged();

            recentAdapter.getData().clear();
            recentAdapter.notifyDataSetChanged();
        }
        mMenuFragment.requestRefresh();
    }

    private class _DATA {
        ArrayList<Visitor> visitor;
        ArrayList<CircleMaster> quanzhu;
    }

}
