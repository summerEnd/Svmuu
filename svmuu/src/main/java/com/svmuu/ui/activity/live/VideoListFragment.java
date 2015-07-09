package com.svmuu.ui.activity.live;

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
import com.sp.lib.widget.list.refresh.PullToRefreshBase;
import com.sp.lib.widget.list.refresh.PullToRefreshRecyclerView;
import com.svmuu.R;
import com.svmuu.common.PageUtils;
import com.svmuu.common.adapter.BaseHolder;
import com.svmuu.common.adapter.video.VideoAdapter;
import com.svmuu.common.entity.Recording;
import com.svmuu.common.http.HttpHandler;
import com.svmuu.common.http.HttpManager;
import com.svmuu.common.http.Response;
import com.svmuu.ui.BaseFragment;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class VideoListFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener<RecyclerView> {
    private String quanzhu_id;

    private VideoAdapter adapter;
    private PullToRefreshRecyclerView refreshRecyclerView;
    PageUtils pageUtils;

    public static VideoListFragment newInstance(String quanzhu_id) {
        VideoListFragment fragment = new VideoListFragment();
        fragment.setQuanzhu_id(quanzhu_id);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("quanzhu_id", quanzhu_id);
    }


    public void setQuanzhu_id(String quanzhu_id) {
        this.quanzhu_id = quanzhu_id;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pageUtils = new PageUtils();

        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    protected void initialize() {

        refreshRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.recyclerView);
        refreshRecyclerView.setOnRefreshListener(this);
        refreshRecyclerView.setPullRefreshEnabled(true);
        refreshRecyclerView.setPullLoadEnabled(true);

        RecyclerView recyclerView = refreshRecyclerView.getRefreshableView();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new VideoAdapter(getActivity(), new ArrayList<Recording>());
        adapter.setListener(new BaseHolder.OnItemListener() {
            @Override
            public void onClick(View itemView, int position) {
                Recording recording = adapter.getData().get(position);
                //                ((LiveActivity) getActivity()).L2V(recording.id,recording.pwd);
                ((LiveActivity) getActivity()).L2V(recording.video_url, recording.pwd);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(3, 3, 3, 3);
            }
        });
        getVideoList(0);

    }

    public void getVideoList(int pn) {
        SRequest request = new SRequest("videolist");
        request.put("quanzhu_id", quanzhu_id);
        request.put("pn", pn);
        HttpManager.getInstance().postMobileApi(getActivity(), request, new HttpHandler() {
            @Override
            public void onResultOk(int statusCode, Header[] headers, Response response) throws JSONException {
                JSONArray array = new JSONArray(response.data);

                List<Recording> data = adapter.getData();
                List<Recording> newData = JsonUtil.getArray(array, Recording.class);
                pageUtils.addNewPage(data, newData);
                adapter.notifyItemRangeInserted(data.size(), newData.size());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                refreshRecyclerView.onPullUpRefreshComplete();
                refreshRecyclerView.onPullDownRefreshComplete();
            }
        });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        pageUtils.setIsRefresh(true);
        getVideoList(0);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        pageUtils.setIsRefresh(false);
        getVideoList(pageUtils.getPage() + 1);
    }
}
