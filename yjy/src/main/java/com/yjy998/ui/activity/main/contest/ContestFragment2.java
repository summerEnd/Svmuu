package com.yjy998.ui.activity.main.contest;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class ContestFragment2 extends BaseFragment implements ViewPager.OnPageChangeListener {


    private SparseArray<BaseListFragment> fragments = new SparseArray<BaseListFragment>();
    private int[] types = new int[]{R.string.my_game, R.string.Elite, R.string.sea_race, R.string.normal_race, R.string.not_race};

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

        for (int i = 0; i < types.length; i++) {
            BaseListFragment fragment = new BaseListFragment();
            String titleId = getString(types[i]);
            fragment.setTitle(titleId);
            fragment.setAdapter(new ContestListAdapter(getActivity(), new ArrayList<Contest>()));
            fragments.append(types[i], fragment);
            adapter.add(fragment);
        }
        pager.setAdapter(adapter);
        pageStrip.setViewPager(pager);
        pageStrip.setPageChangeListener(this);
        getContestList();
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        ContestListAdapter adapter = (ContestListAdapter) fragments.get(types[i]).getAdapter();
        if (adapter.getData().size() == 0) {
            if (i == 0) {
                getMyContestList();
            } else {
                getContestList();
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

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
                    ArrayList<Contest> array = JsonUtil.getArray(data, Contest.class);
                    //清空数据
                    for (int i = 1; i < types.length; i++) {
                        ((ContestListAdapter) fragments.get(types[i]).getAdapter()).getData().clear();
                    }

                    //按类型将比赛分类
                    for (Contest contest : array) {
                        int type = contest.getType();
                        ContestListAdapter adapter = (ContestListAdapter) fragments.get(type).getAdapter();
                        adapter.getData().add(contest);
                    }
                    //刷新列表
                    for (int i = 1; i < types.length; i++) {
                        ((ContestListAdapter) fragments.get(types[i]).getAdapter()).notifyDataSetChanged();
                    }

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
                    ContestListAdapter adapter = (ContestListAdapter) fragments.get(R.string.my_game).getAdapter();
                    JSONArray data = new JSONArray(response.data);
                    JsonUtil.getArray(data, Contest.class, adapter.getData());
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
