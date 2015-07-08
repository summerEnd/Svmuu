package com.svmuu.ui.activity.box;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.widget.list.refresh.PullToRefreshBase;
import com.sp.lib.widget.list.refresh.PullToRefreshRecyclerView;
import com.svmuu.R;
import com.svmuu.common.PageUtils;
import com.svmuu.common.adapter.box.BoxListAdapter;
import com.svmuu.common.entity.Box;
import com.svmuu.common.http.HttpHandler;
import com.svmuu.common.http.HttpManager;
import com.svmuu.common.http.Response;
import com.svmuu.ui.BaseFragment;
import com.svmuu.ui.activity.SecondActivity;
import com.svmuu.ui.pop.ProgressIDialog;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.State;

public class BoxActivity extends SecondActivity {

    private CheckedTextView textBox;
    private CheckedTextView videoBox;
    private final String TAG_VIDEO_LIST = "video";
    private final String TAG_TEXT_LIST = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);
        if (savedInstanceState == null) {
            BoxListFragment fragment = BoxListFragment.withType(2);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, BoxListFragment.withType(1), TAG_TEXT_LIST)
                    .add(R.id.fragmentContainer, fragment, TAG_VIDEO_LIST)
                    .hide(fragment)
                    .commit();
        }
        initialize();

    }

    private void initialize() {

        textBox = (CheckedTextView) findViewById(R.id.textBox);
        videoBox = (CheckedTextView) findViewById(R.id.videoBox);

        textBox.setOnClickListener(this);
        videoBox.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.textBox: {
                videoBox.setChecked(false);
                textBox.setChecked(true);
                ft
                        .hide(fm.findFragmentByTag(TAG_VIDEO_LIST))
                        .show(fm.findFragmentByTag(TAG_TEXT_LIST))
                ;
                break;
            }
            case R.id.videoBox: {
                videoBox.setChecked(true);
                textBox.setChecked(false);
                ft
                        .hide(fm.findFragmentByTag(TAG_TEXT_LIST))
                        .show(fm.findFragmentByTag(TAG_VIDEO_LIST))
                ;
                break;
            }
        }
        ft.commit();

        super.onClick(v);
    }


    public static class BoxListFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener<RecyclerView> {
        private RecyclerView recyclerView;
        private BoxListAdapter adapter;
        PullToRefreshRecyclerView refreshView;
        int type;
        private boolean doRefresh = false;
        PageUtils mPageUtil;

        /**
         * @param type 1：文字 2：视频  默认为1
         */
        public static BoxListFragment withType(int type) {
            BoxListFragment fragment = new BoxListFragment();
            fragment.setType(type);
            return fragment;
        }

        public BoxListFragment() {
            mPageUtil = new PageUtils();

        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            refreshView = new PullToRefreshRecyclerView(getActivity());

            refreshView.setPullLoadEnabled(true);
            refreshView.setPullRefreshEnabled(true);
            refreshView.setOnRefreshListener(this);
            recyclerView = refreshView.getRefreshableView();
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            adapter = new BoxListAdapter(getActivity(), new ArrayList<Box>());
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
                    outRect.set(6, 6, 6, 6);
                }
            });
            return refreshView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            getBoxList(0);
        }


        /**
         * @param page 分页的标志
         */
        public void getBoxList(int page) {
            SRequest request = new SRequest("getMyBookBox");
            request.put("page", page);
            request.put("type", type);
            HttpManager.getInstance().getMobileApi(getActivity(), request, new HttpHandler() {
                @Override
                public void onResultOk(int statusCode, Header[] headers, Response response) throws JSONException {
                    List<Box> newData = JsonUtil.getArray(new JSONArray(response.data), Box.class);
                    mPageUtil.addNewPage(adapter.getData(), newData, doRefresh);
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
            doRefresh = true;
            getBoxList(0);

        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
            getBoxList(mPageUtil.getPage() + 1);
        }

        @Override
        public void onHiddenChanged(boolean hidden) {
            super.onHiddenChanged(hidden);
            if (!hidden && adapter.getData().size() == 0) {
                getBoxList(0);
            }
        }
    }

}
