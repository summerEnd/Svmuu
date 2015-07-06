package com.svmuu.ui.activity.live;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.widget.list.refresh.PullToRefreshBase;
import com.sp.lib.widget.list.refresh.PullToRefreshRecyclerView;
import com.svmuu.common.adapter.live.LiveAdapter;
import com.svmuu.common.entity.Live;
import com.svmuu.common.http.HttpHandler;
import com.svmuu.common.http.HttpManager;
import com.svmuu.common.http.Response;
import com.svmuu.ui.BaseFragment;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;

public class LiveListFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener<RecyclerView> {
    RecyclerView recyclerView;
    private LiveAdapter adapter;
    ArrayList<Live> lives = new ArrayList<>();
    private PullToRefreshRecyclerView contentView;
    private String url;
    private String kw;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        contentView = new PullToRefreshRecyclerView(getActivity());
        contentView.setOnRefreshListener(this);
        contentView.setPullRefreshEnabled(true);

        recyclerView = contentView.getRefreshableView();
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(manager);
        adapter = new LiveAdapter(getActivity(), lives);
        recyclerView.setAdapter(adapter);
        return contentView;
    }

    @Override
    protected void initialize() {
        requestRefresh();
    }

    /**
     * @param kw 搜索的关键词
     */
    private void search(String url, String kw) {

        SRequest request = new SRequest(url);
        request.put("kw", kw);
        HttpManager.getInstance().postMobileApi(getActivity(), request, new HttpHandler() {
            @Override
            public void onResultOk(int statusCOde, Header[] headers, Response response) throws JSONException {
                lives.clear();
                if (response.data.startsWith("[")) {
                    JsonUtil.getArray(new JSONArray(response.data), Live.class, lives);
                    adapter.sortByHot(false);
                } else {
                    JSONObject data = new JSONObject(response.data);
                    ArrayList<Live> onlineList = JsonUtil.getArray(data.getJSONArray("online"), Live.class);
                    ArrayList<Live> offlineList = JsonUtil.getArray(data.getJSONArray("offline"), Live.class);
                    for (Live live : onlineList) {
                        live.isOnline = true;
                    }
                    for (Live live : offlineList) {
                        live.isOnline = false;
                    }
                    lives.addAll(onlineList);
                    lives.addAll(offlineList);
                    adapter.sortByHot(true);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                contentView.onPullDownRefreshComplete();
                contentView.onPullUpRefreshComplete();
            }
        });
    }

    /**
     * @param url 搜索的链接
     * @param kw  搜索的关键字
     */
    public void setUrlAndKey(String url, String kw) {
        this.kw = kw;
        this.url = url;
    }

    @Override
    protected void refresh() {
        search(url, kw);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        requestRefresh();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

    }

}
