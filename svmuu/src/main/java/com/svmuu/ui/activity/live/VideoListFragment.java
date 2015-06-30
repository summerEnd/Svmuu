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
    private RecyclerView recyclerView;
    private String quanzhu_id;
    private int page = 0;
    private boolean isRefresh = false;
    private VideoAdapter adapter;
    private PullToRefreshRecyclerView refreshRecyclerView;

    public static VideoListFragment newInstance(String quanzhu_id) {
        VideoListFragment fragment = new VideoListFragment();
        fragment.setQuanzhu_id(quanzhu_id);
        return fragment;
    }

    public void setQuanzhu_id(String quanzhu_id) {
        this.quanzhu_id = quanzhu_id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    protected void initialize() {

        refreshRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.recyclerView);
        refreshRecyclerView.setOnRefreshListener(this);
        refreshRecyclerView.setPullRefreshEnabled(true);
        refreshRecyclerView.setPullLoadEnabled(true);

        recyclerView = refreshRecyclerView.getRefreshableView();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new VideoAdapter(getActivity(), new ArrayList<Recording>());
        adapter.setListener(new BaseHolder.OnItemListener() {
            @Override
            public void onClick(View itemView, int position) {
                Recording recording = adapter.getData().get(position);
//                ((LiveActivity) getActivity()).L2V(recording.id,recording.pwd);
                ((LiveActivity) getActivity()).L2V("30d998bdd65b4a2bb6c405cad9d8dee5", "964786");
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(3, 3, 3, 3);
            }
        });
        requestRefresh();

    }

    @Override
    protected void refresh() {
        getVideoList(page);
    }

    public void getVideoList(int pn) {
        SRequest request = new SRequest("videolist");
        request.put("quanzhu_id", quanzhu_id);
        request.put("pn", pn);
        HttpManager.getInstance().postMobileApi(getActivity(), request, new HttpHandler() {
            @Override
            public void onResultOk(int statusCode, Header[] headers, Response response) throws JSONException {

                List<Recording> data = adapter.getData();
                JSONArray array = new JSONArray(response.data);
                if (isRefresh){
                    data.clear();
                    isRefresh=false;
                    page=0;
                    JsonUtil.getArray(array,Recording.class, data);
                    adapter.notifyDataSetChanged();
                }else{
                    page++;
                    int position=data.size();
                    JsonUtil.getArray(array,Recording.class, data);
                    adapter.notifyItemRangeInserted(position,array.length());
                }


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
        isRefresh = true;
        getVideoList(0);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        isRefresh = false;
        getVideoList(page + 1);
    }
}
