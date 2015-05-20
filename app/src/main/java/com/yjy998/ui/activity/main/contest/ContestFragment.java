package com.yjy998.ui.activity.main.contest;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.widget.pager.title.PageStrip;
import com.yjy998.R;
import com.yjy998.common.adapter.ContestListAdapter;
import com.yjy998.common.adapter.FragmentPagerAdapter;
import com.yjy998.common.entity.Contest;
import com.yjy998.common.entity.ContractDetail;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.base.BaseFragment;
import com.yjy998.ui.activity.base.BaseListFragment;
import com.yjy998.ui.activity.main.apply.ApplyActivity;
import com.yjy998.ui.activity.main.apply.ContestApplyActivity;
import com.yjy998.ui.activity.main.my.ContractInfoActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class ContestFragment extends BaseFragment implements ViewPager.OnPageChangeListener {


    private ContestListAdapter allGameAdapter;
    private ContestListAdapter myGameAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_real_game, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initialize();
    }

    private void initialize() {
        PageStrip pageStrip = (PageStrip) getActivity().findViewById(R.id.pageStrip);
        ViewPager pager = (ViewPager) getActivity().findViewById(R.id.pager);

        allGameAdapter = getAdapter();
        myGameAdapter = getAdapter();

        BaseListFragment mGameListFragment = new BaseListFragment();
        mGameListFragment.setTitle(getString(R.string.contest));
        mGameListFragment.setAdapter(allGameAdapter);
        mGameListFragment.setOnItemClickListener(new OnGameListClick());

        BaseListFragment mMyGameList = new BaseListFragment();
        mMyGameList.setTitle(getString(R.string.contest));
        mMyGameList.setAdapter(myGameAdapter);
        mMyGameList.setOnItemClickListener(new OnMyGameListClick());

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager());
        adapter.add(mGameListFragment);
        adapter.add(mMyGameList);
        pager.setAdapter(adapter);
        pageStrip.setViewPager(pager);
        pageStrip.setPageChangeListener(this);
        getContestList();
    }

    ContestListAdapter getAdapter() {

        return new ContestListAdapter(getActivity(), new ArrayList<Contest>());
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        if (i == 1) {
            if (myGameAdapter.getData().size() == 0) {
                getMyContestList();
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private class OnGameListClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startActivity(new Intent(view.getContext(), ContestApplyActivity.class)
                    .putExtra(ContestApplyActivity.EXTRA_CONTEST, allGameAdapter.getData().get(position)));
        }
    }

    private class OnMyGameListClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startActivity(new Intent(view.getContext(), ContractInfoActivity.class)
                    .putExtra(ContractInfoActivity.EXTRA_CONTRACT_NO,myGameAdapter.getData().get(position).id));
        }
    }

    /**
     * 获取大赛列表
     */
    private void getContestList() {
        SRequest request = new SRequest();
        request.setUrl("http://mobile.yjy998.com/h5/contest/contestlist");
        YHttpClient.getInstance().post(getActivity(), request, new YHttpHandler(false) {
            @Override
            protected void onStatusCorrect(Response response) {
                try {
                    JSONArray data = new JSONArray(response.data);
                    JsonUtil.getArray(data, Contest.class, allGameAdapter.getData());
                    allGameAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取我参加的大赛列表
     */
    private void getMyContestList() {
        SRequest request = new SRequest();
        request.setUrl("http://mobile.yjy998.com/h5/contest/mycontest");
        YHttpClient.getInstance().post(getActivity(), request, new YHttpHandler(false) {
            @Override
            protected void onStatusCorrect(Response response) {
                try {
                    JSONArray data = new JSONArray(response.data);
                    JsonUtil.getArray(data, Contest.class, myGameAdapter.getData());
                    myGameAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
