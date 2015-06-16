package com.yjy998.ui.activity.main.contest;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.widget.list.refresh.PullToRefreshBase;
import com.sp.lib.widget.nav.title.PageStrip;
import com.yjy998.R;
import com.yjy998.common.adapter.ContestListAdapter;
import com.yjy998.common.adapter.FragmentPagerAdapter;
import com.yjy998.common.entity.Contest;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.base.BaseFragment;
import com.yjy998.ui.activity.base.BaseListFragment;
import com.yjy998.ui.activity.main.apply.ContestApplyActivity;
import com.yjy998.ui.activity.main.my.ContractInfoActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class ContestFragment2 extends BaseFragment implements ViewPager.OnPageChangeListener {


    private ArrayList<BaseListFragment> fragments = new ArrayList<>();
    private int[] types = new int[]{R.string.Elite, R.string.sea_race, R.string.circle_race, R.string.my_game};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_real_game2, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initialize();
    }

    private void initialize() {
        PageStrip pageStrip = (PageStrip) getActivity().findViewById(R.id.pageStrip);
        ViewPager pager = (ViewPager) getActivity().findViewById(R.id.pager);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager());

        for (int i=0;i<types.length ; i++) {
            BaseListFragment fragment = createFragment(""+(i+1), types[i]);
            if (i==0){
                fragment.setDoRefreshWhenCreated(true);
            }
            adapter.add(fragment);

        }
        pager.setAdapter(adapter);
        pageStrip.setViewPager(pager);
        pageStrip.setPageChangeListener(this);
    }

    BaseListFragment createFragment(String type, int titleId) {
        GameListFragment fragment;
        if (titleId == R.string.my_game) {
            fragment = new MyGameList();
            fragment.setOnItemClickListener(new OnMyGameListClick());
        } else {
            fragment = new GameListFragment();
            fragment.setOnItemClickListener(new OnGameListClick());
        }
        fragment.setType(type);
        fragment.setTitle(getString(titleId));

        fragment.setAdapter(new ContestListAdapter(getActivity(), new ArrayList<Contest>()));
        fragments.add(fragment);
        return fragment;
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        BaseListFragment fragment = fragments.get(i);
        ContestListAdapter adapter = (ContestListAdapter) fragment.getAdapter();
        if (adapter.getData().size() == 0) {
            fragment.refresh();
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    private class OnGameListClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            ContestListAdapter adapter = (ContestListAdapter) parent.getAdapter();

            startActivity(new Intent(view.getContext(), ContestApplyActivity.class)
                    .putExtra(ContestApplyActivity.EXTRA_CONTEST, adapter.getData().get(position)));
        }
    }

    private class OnMyGameListClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ContestListAdapter adapter = (ContestListAdapter) parent.getAdapter();
            startActivity(new Intent(view.getContext(), ContractInfoActivity.class)
                    .putExtra(ContractInfoActivity.EXTRA_CONTRACT_NO, adapter.getData().get(position).prodId));
        }
    }

    public static class GameListFragment extends BaseListFragment implements PullToRefreshBase.OnRefreshListener<ListView> {
        int page = 1;
        boolean doRefresh = false;
        protected String url = "http://mobile.yjy998.com/h5/contest/contestlist";
        private String type;

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            refreshList.setPullLoadEnabled(true);
            refreshList.setBackgroundColor(Color.WHITE);
            refreshList.setOnRefreshListener(this);
            if (isDoRefreshWhenCreated()){
                refresh();
                setDoRefreshWhenCreated(false);
            }
        }

        /**
         * 获取我参加的大赛列表
         */
        public void getMyContestList(int pn) {
            SRequest request = new SRequest();
            request.setUrl(url);
            request.put("pn", pn);
            request.put("contest_type", type);
            YHttpClient.getInstance().post(getActivity(), request, new YHttpHandler(false) {
                @Override
                protected void onStatusCorrect(Response response) {
                    try {
                        ContestListAdapter adapter = (ContestListAdapter) getAdapter();

                        List<Contest> contestList = adapter.getData();
                        JSONArray data = new JSONArray(response.data);

                        ArrayList<Contest> newData = new ArrayList<>();

                        JsonUtil.getArray(data, Contest.class, newData);
                        if (newData.size() == 0) {
                            refreshList.setHasMoreData(false);
                        } else {
                            refreshList.setHasMoreData(true);
                        }

                        if (doRefresh) {
                            contestList.clear();
                            page = 1;

                        } else {

                            if (newData.size() != 0) {
                                page++;
                            }
                        }
                        contestList.addAll(newData);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    refreshList.onPullUpRefreshComplete();
                    refreshList.onPullDownRefreshComplete();
                }
            });
        }

        @Override
        public void refresh() {
            ContestListAdapter adapter = (ContestListAdapter) getAdapter();
            if (adapter.getData().size()==0){
                doRefresh=true;
                getMyContestList(1);
            }
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            doRefresh = true;
            getMyContestList(1);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            doRefresh = false;
            getMyContestList(page + 1);
        }
    }

    public static class MyGameList extends GameListFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            url = "http://mobile.yjy998.com/h5/contest/mycontest";
        }
    }
}
