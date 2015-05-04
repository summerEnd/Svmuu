package com.yjy998.ui.activity.contest;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.sp.lib.widget.pager.title.PageStrip;
import com.yjy998.R;
import com.yjy998.adapter.FragmentPagerAdapter;
import com.yjy998.adapter.GameListAdapter;
import com.yjy998.entity.Contest;
import com.yjy998.ui.activity.other.BaseFragment;
import com.yjy998.ui.activity.other.BaseListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class ContestFragment extends BaseFragment {


    private PageStrip pageStrip;
    private ViewPager pager;
    private BaseListFragment mGameListFragment;
    private BaseListFragment mMyGameList;

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
        pageStrip = (PageStrip) getActivity().findViewById(R.id.pageStrip);
        pager = (ViewPager) getActivity().findViewById(R.id.pager);

        ListAdapter listAdapter = getAdapter();

        mGameListFragment = new BaseListFragment();
        mGameListFragment.setTitle(getString(R.string.gameList));
        mGameListFragment.setAdapter(listAdapter);
        mGameListFragment.setOnItemClickListener(new OnGameListClick());

        mMyGameList = new BaseListFragment();
        mMyGameList.setTitle(getString(R.string.my_game));
        mMyGameList.setAdapter(listAdapter);
        mMyGameList.setOnItemClickListener(new OnMyGameListClick());

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager());
        adapter.add(mGameListFragment);
        adapter.add(mMyGameList);
        pager.setAdapter(adapter);
        pageStrip.setViewPager(pager);
    }

    ListAdapter getAdapter() {
        List<Contest> data = new ArrayList<Contest>();
        for (int i = 0; i < 20; i++) {
            data.add(new Contest());
        }
        GameListAdapter gameListAdapter = new GameListAdapter(getActivity(), data);

        return gameListAdapter;
    }

    private class OnGameListClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startActivity(new Intent(view.getContext(), ContestInfoActivity.class));
        }
    }

    private class OnMyGameListClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startActivity(new Intent(view.getContext(), ContestInfoActivity.class));
        }
    }
}
