package com.svmuu.ui.activity.box;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.widget.list.refresh.PullToRefreshBase;
import com.sp.lib.widget.list.refresh.PullToRefreshRecyclerView;
import com.svmuu.R;
import com.svmuu.common.PageUtils;
import com.svmuu.common.adapter.video.VideoAdapterForBox;
import com.svmuu.common.entity.Box;
import com.svmuu.common.entity.BoxVideoDetail;
import com.svmuu.common.http.HttpHandler;
import com.svmuu.common.http.HttpManager;
import com.svmuu.common.http.Response;
import com.svmuu.ui.activity.SecondActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 视频宝盒页面
 */
public class VideoBoxActivity extends SecondActivity implements PullToRefreshBase.OnRefreshListener<RecyclerView> {
    public static final String EXTRA_ID = "id";

    private TextView titleText;
    private TextView summaryText;
    PullToRefreshRecyclerView refreshView;

    private String boxId;
    PageUtils pageUtils;
    private VideoAdapterForBox adapter;


    private Box info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_info);
        initialize();
    }

    private void initialize() {
        pageUtils = new PageUtils();
        titleText = (TextView) findViewById(R.id.titleText);
        summaryText = (TextView) findViewById(R.id.summaryText);
        refreshView = (PullToRefreshRecyclerView) findViewById(R.id.refreshView);
        refreshView.setPullLoadEnabled(true);
        refreshView.setPullRefreshEnabled(true);
        refreshView.setOnRefreshListener(this);
        //列表
        RecyclerView recyclerView = refreshView.getRefreshableView();
        adapter = new VideoAdapterForBox(this, new ArrayList<BoxVideoDetail>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(6, 6, 6, 6);
            }
        });

        boxId = getIntent().getStringExtra(EXTRA_ID);
        setInfo(null);
        if (TextUtils.isEmpty(boxId)) {
            ContextUtil.toast(R.string.box_detail_get_failed);
        } else {
            getDetail(0);
        }
    }

    public void getDetail(int page) {
        final SRequest request = new SRequest("getVideoBoxDetail");
        request.put("boxid", boxId);
        request.put("page", page);

        HttpManager.getInstance().getMobileApi(this, request, new HttpHandler() {
            @Override
            public void onResultOk(int statusCode, Header[] headers, Response response) throws JSONException {
                JSONObject object = new JSONObject(response.data);
                if (info == null) {
                    info = JsonUtil.get(object.getJSONObject("info"), Box.class);
                    setInfo(info);
                }
                ArrayList<BoxVideoDetail> newData = JsonUtil.getArray(object.getJSONArray("data"), BoxVideoDetail.class);
                pageUtils.addNewPage(adapter.getData(), newData);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                refreshView.onPullDownRefreshComplete();
                refreshView.onPullUpRefreshComplete();
            }
        });
    }

    public void setInfo(Box info) {
        if (info != null) {
            summaryText.setText(info.desc);
            titleText.setText(info.name);
        } else {
            summaryText.setText("");
            titleText.setText("");
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        pageUtils.setIsRefresh(true);
        getDetail(0);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        pageUtils.setIsRefresh(false);
        getDetail(pageUtils.getPage() + 1);
    }


}
