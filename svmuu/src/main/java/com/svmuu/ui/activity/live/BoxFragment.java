package com.svmuu.ui.activity.live;

import android.app.Dialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.common.util.TextPainUtil;
import com.sp.lib.widget.list.refresh.PullToRefreshBase;
import com.sp.lib.widget.list.refresh.PullToRefreshRecyclerView;
import com.svmuu.R;
import com.svmuu.common.PageUtils;
import com.svmuu.common.adapter.box.BoxGridAdapter;
import com.svmuu.common.adapter.decoration.EmptyDecoration;
import com.svmuu.common.entity.Box;
import com.svmuu.common.http.HttpHandler;
import com.svmuu.common.http.HttpManager;
import com.svmuu.common.http.Response;
import com.svmuu.ui.BaseFragment;
import com.svmuu.ui.pop.ProgressIDialog;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的宝盒
 * 暂时更改为我的订阅
 */
public class BoxFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener<RecyclerView> {
    RecyclerView recyclerView;
    PageUtils pageUtils;
    private BoxGridAdapter adapter;

    private PullToRefreshRecyclerView refreshView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        refreshView = new PullToRefreshRecyclerView(getActivity());
        refreshView.setOnRefreshListener(this);
        refreshView.setBackgroundColor(Color.WHITE);
        refreshView.setPullLoadEnabled(true);
        refreshView.setPullRefreshEnabled(true);
        pageUtils = new PageUtils();

        recyclerView = refreshView.getRefreshableView();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        adapter = new BoxGridAdapter(getActivity(), new ArrayList<Box>());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new EmptyDecoration(getActivity(),getString(R.string.empty_book)));
        return refreshView;
    }

    @Override
    protected void initialize() {
        getBoxList(0);
    }

    /**
     * @param page 分页的标志
     */
    public void getBoxList(int page) {
        SRequest request = new SRequest("getMyBookBox");
        request.put("page", page);
        request.put("type", 0);
        HttpManager.getInstance().getMobileApi(getActivity(), request, new HttpHandler() {
            @Override
            public void onResultOk(int statusCode, Header[] headers, Response response) throws JSONException {
                List<Box> newData = JsonUtil.getArray(new JSONArray(response.data), Box.class);
                pageUtils.addNewPage(adapter.getData(), newData);
                adapter.notifyDataSetChanged();
            }

            @Override
            public Dialog onCreateDialog() {
                return new ProgressIDialog(getActivity());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                refreshView.onPullDownRefreshComplete();
                refreshView.onPullUpRefreshComplete();
            }
        });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        pageUtils.setIsRefresh(true);
        getBoxList(0);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        pageUtils.setIsRefresh(false);
        getBoxList(pageUtils.getPage() + 1);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && adapter.getData().size() == 0) {
            getBoxList(0);
        }
    }
}
