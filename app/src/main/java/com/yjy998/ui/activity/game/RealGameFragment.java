package com.yjy998.ui.activity.game;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.sp.lib.widget.PagerSlidingTabStrip;
import com.yjy998.R;
import com.yjy998.adapter.FragmentPagerAdapter;
import com.yjy998.adapter.GameListAdapter;
import com.yjy998.entity.Game;
import com.yjy998.ui.activity.other.BaseFragment;
import com.yjy998.ui.activity.other.BaseListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class RealGameFragment extends BaseFragment {


    private PagerSlidingTabStrip tabStrip;
    private ViewPager pager;
    private BaseListFragment mGameListFragment;
    private BaseListFragment mMyGameList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_tab_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initialize();
    }

    private void initialize() {
        tabStrip = (PagerSlidingTabStrip) getActivity().findViewById(R.id.tabStrip);
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
        tabStrip.setViewPager(pager);

    }

    ListAdapter getAdapter() {
        List<Game> data = new ArrayList<Game>();
        for (int i = 0; i < 20; i++) {
            data.add(new Game());
        }
        GameListAdapter gameListAdapter = new GameListAdapter(getActivity(), data);

        return gameListAdapter;
    }

    private class OnGameListClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startActivity(new Intent(view.getContext(), GameInfoActivity.class));
        }
    }

    private class OnMyGameListClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startActivity(new Intent(view.getContext(), GameInfoActivity.class));
        }
    }
}
