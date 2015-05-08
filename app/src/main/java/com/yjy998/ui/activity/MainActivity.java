package com.yjy998.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.yjy998.R;
import com.yjy998.ui.activity.apply.ApplyFragment;
import com.yjy998.ui.activity.contest.ContestFragment;
import com.yjy998.ui.activity.home.HomeFragment;
import com.yjy998.ui.activity.more.MoreFragment;
import com.yjy998.ui.activity.my.CenterFragment;
import com.yjy998.ui.activity.other.BaseFragment;
import com.yjy998.ui.activity.other.MenuActivity;
import com.yjy998.ui.view.TabItem;

import static com.yjy998.ui.view.TabItem.CheckListener;


public class MainActivity extends MenuActivity implements HomeFragment.HomeListener {

    public static final String EXTRA_CHECK_TAB_ID = "EXTRA_CHECK_TAB_ID";
    public static final int ID_HOME = R.id.tabHome;
    public static final int ID_GAME = R.id.tabGame;
    public static final int ID_APPLY = R.id.tabApply;
    public static final int ID_CENTER = R.id.tabCenter;
    public static final int ID_MORE = R.id.tabMore;

    private TabItem curTab;
    HomeFragment mHome;
    ContestFragment mGameFragment;
    ApplyFragment mApplyFragment;
    CenterFragment mCenterFragment;
    MoreFragment mMoreFragment;
    Fragment displayingFragment;


    TabItem tabGame;
    TabItem tabMore;
    TabItem tabApply;
    TabItem tabHome;
    TabItem tabCenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int id = intent.getIntExtra(EXTRA_CHECK_TAB_ID, 0);

        TabItem tabItem = (TabItem) findViewById(id);
        if (tabItem != null) {
            tabItem.performClick();
        }
    }

    private void initialize() {

        tabGame = (TabItem) findViewById(R.id.tabGame);
        tabMore = (TabItem) findViewById(R.id.tabMore);
        tabApply = (TabItem) findViewById(R.id.tabApply);
        tabHome = (TabItem) findViewById(R.id.tabHome);
        tabCenter = (TabItem) findViewById(R.id.tabCenter);

        tabGame.setCheckListener(listener);
        tabMore.setCheckListener(listener);
        tabHome.setCheckListener(listener);
        tabApply.setCheckListener(listener);
        tabCenter.setCheckListener(listener);

        tabHome.performClick();
    }


    private CheckListener listener = new CheckListener() {
        @Override
        public void onSelected(TabItem view) {

            if (curTab != null && curTab != view) {
                curTab.setChecked(false);
            }
            curTab = view;
            BaseFragment fragment = null;
            switch (view.getId()) {
                case R.id.tabHome: {


                    if (mHome == null) {
                        mHome = new HomeFragment();

                    }
                    fragment = mHome;

                    break;
                }
                case R.id.tabApply: {
                    if (mApplyFragment == null) {
                        mApplyFragment = new ApplyFragment();
                    }
                    fragment = mApplyFragment;
                    break;
                }
                case R.id.tabGame: {
                    if (mGameFragment == null) {
                        mGameFragment = new ContestFragment();
                    }
                    fragment = mGameFragment;
                    break;
                }
                case R.id.tabCenter: {
                    if (mCenterFragment == null) {
                        mCenterFragment = new CenterFragment();
                    }
                    fragment = mCenterFragment;
                    break;
                }
                case R.id.tabMore: {
                    if (mMoreFragment == null) {
                        mMoreFragment = new MoreFragment();
                    }
                    fragment = mMoreFragment;
                    break;
                }
            }

            if (fragment != null && fragment != displayingFragment) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                if (fragment.isAdded()) {
                    ft.show(fragment);
                } else {
                    ft.add(R.id.fragmentContainer, fragment);
                }

                if (displayingFragment != null) {
                    ft.hide(displayingFragment);
                }
                displayingFragment = fragment;
                ft.commit();
            }
        }
    };

    @Override
    public void onHomeFragmentClick(View v) {
        switch (v.getId()) {
            case R.id.myGame: {
                tabCenter.performClick();
                break;
            }
            case R.id.realGame: {
                tabGame.performClick();
                break;
            }

        }
    }

    @Override
    public boolean onMenuClick(View v) {

        switch (v.getId()) {

            case R.id.center: {
                tabCenter.performClick();
                break;
            }
            case R.id.realGame: {
                tabGame.performClick();
                break;
            }
            case R.id.help: {
                tabMore.performClick();
                break;
            }
            default:
                return super.onMenuClick(v);
        }
        close();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLayout();
    }

    @Override
    protected void refreshLayout() {
        mMenuFragment.refresh();
        if (mCenterFragment != null && mCenterFragment.getView() != null) {
            mCenterFragment.refresh();
        }
    }
}
