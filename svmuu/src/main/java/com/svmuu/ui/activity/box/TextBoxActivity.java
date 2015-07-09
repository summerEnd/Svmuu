package com.svmuu.ui.activity.box;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.widget.list.refresh.PullToRefreshBase;
import com.sp.lib.widget.list.refresh.PullToRefreshRecyclerView;
import com.svmuu.R;
import com.svmuu.common.PageUtils;
import com.svmuu.common.adapter.DividerDecoration;
import com.svmuu.common.adapter.box.TextBoxAdapter;
import com.svmuu.common.entity.Box;
import com.svmuu.common.entity.TextBoxDetail;
import com.svmuu.common.http.HttpHandler;
import com.svmuu.common.http.HttpManager;
import com.svmuu.common.http.Response;
import com.svmuu.ui.activity.SecondActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TextBoxActivity extends SecondActivity implements PullToRefreshBase.OnRefreshListener<RecyclerView> {
    public static final String EXTRA_ID = "id";
    private PullToRefreshRecyclerView refreshView;
    PageUtils pageUtils;
    String boxId;

    private TextBoxAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_box);
        initialize();
    }

    private void initialize() {
        refreshView = (PullToRefreshRecyclerView) findViewById(R.id.refreshView);
        refreshView.setPullLoadEnabled(true);
        refreshView.setPullRefreshEnabled(true);
        refreshView.setOnRefreshListener(this);

        RecyclerView recyclerView = refreshView.getRefreshableView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerDecoration(this));
        adapter = new TextBoxAdapter(this, new ArrayList<TextBoxDetail>());
        adapter.setHeadInfo("","");
        recyclerView.setAdapter(adapter);

        boxId = getIntent().getStringExtra(EXTRA_ID);
        pageUtils = new PageUtils();
        getDetail(0);
    }

    public void getDetail(int page) {
        SRequest request = new SRequest("getTextBoxDetail");
        request.put("boxid", boxId);
        request.put("page", page);
        HttpManager.getInstance().getMobileApi(this, request, new HttpHandler() {
            @Override
            public void onResultOk(int statusCode, Header[] headers, Response response) throws JSONException {
                JSONObject data = new JSONObject(response.data);
                JsonUtil.debugJsonObject(data);
                List<TextBoxDetail> newData = JsonUtil.getArray(data.getJSONArray("data"), TextBoxDetail.class);
                pageUtils.addNewPage(adapter.getData(), newData);
                Box info=JsonUtil.get(data.getJSONObject("info"),Box.class);
                adapter.setHeadInfo(info.name,info.desc);

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
