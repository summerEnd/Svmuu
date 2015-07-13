package com.svmuu.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.common.util.SLog;
import com.sp.lib.widget.list.LinearListView;
import com.sp.lib.widget.list.refresh.PullToRefreshBase;
import com.sp.lib.widget.list.refresh.PullToRefreshScrollView;
import com.svmuu.AppDelegate;
import com.svmuu.R;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.adapter.master.RecentAdapter;
import com.svmuu.common.adapter.other.RecommendAdapter;
import com.svmuu.common.config.Constant;
import com.svmuu.common.entity.CircleMaster;
import com.svmuu.common.entity.Visitor;
import com.svmuu.common.http.HttpHandler;
import com.svmuu.common.http.HttpManager;
import com.svmuu.common.http.Response;
import com.svmuu.ui.activity.live.LiveActivity;
import com.svmuu.ui.activity.live.MyCircleActivity;
import com.svmuu.ui.widget.CustomSearchView;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MenuActivity implements CustomSearchView.Callback, PullToRefreshBase.OnRefreshListener {


    private LinearLayout recentContainer;
    private LinearListView list;
    _DATA mData;
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
        refreshScrollView.setOnRefreshListener(this);
        setContentView(refreshScrollView);
        initialize();
    }

    private void initialize() {

        CustomSearchView searchView = (CustomSearchView) findViewById(R.id.searchView);
        recentContainer = (LinearLayout) findViewById(R.id.recentContainer);
        list = (LinearListView) findViewById(R.id.list);
        recommendAdapter = new RecommendAdapter(this, new ArrayList<CircleMaster>());
        list.setAdapter(recommendAdapter);


        searchView.setCallback(this);

        findViewById(R.id.liveRoom).setOnClickListener(this);
        findViewById(R.id.realContest).setOnClickListener(this);
        findViewById(R.id.stockSchool).setOnClickListener(this);
        findViewById(R.id.center).setOnClickListener(this);

        getRecent();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getRecent();
    }

    @Override
    public void onSearch(String key) {

    }

    @Override
    public void onEdit(String key) {

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

                displayRecent(mData.visitor);
                recommendAdapter.notifyDataSetChanged();
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

            displayRecent(null);
        }
        mMenuFragment.requestRefresh();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        getRecent();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    private class _DATA {
        ArrayList<Visitor> visitor;
        ArrayList<CircleMaster> quanzhu;
    }

    /**
     * 展示最近访问
     *
     * @param visitors 可以传null
     */
    void displayRecent(@Nullable List<Visitor> visitors) {

        for (int i = 0; i < recentContainer.getChildCount(); i++) {
            recentContainer.getChildAt(i).setVisibility(View.INVISIBLE);
        }

        if (visitors == null || visitors.size() == 0) {
            return;
        }

        DisplayImageOptions options = ImageOptions.getRoundCorner(5);
        for (int i = 0; i < visitors.size(); i++) {
            if (i >= 5) {
                break;
            }
            Visitor visitor = visitors.get(i);

            ViewGroup child = (ViewGroup) recentContainer.getChildAt(i);
            TextView name = (TextView) child.findViewById(R.id.tv_name);
            ImageView avatar = (ImageView) child.findViewById(R.id.iv_avatar);
            name.setText(visitor.unick);
            ImageLoader.getInstance().displayImage(visitor.uface, avatar, options);
            child.setTag(visitor);
            child.setVisibility(View.VISIBLE);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    context.startActivity(new Intent(context, LiveActivity.class)
                            .putExtra(LiveActivity.EXTRA_QUANZHU_ID, ((Visitor) v.getTag()).id));
                }
            });
        }

    }
}
