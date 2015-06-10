package com.yjy998.ui.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.yjy998.R;
import com.yjy998.common.util.VersionUtil;
import com.yjy998.ui.activity.base.BaseFragment;
import com.yjy998.ui.activity.base.MenuActivity;
import com.yjy998.ui.activity.main.apply.ApplyFragment;
import com.yjy998.ui.activity.main.contest.ContestFragment2;
import com.yjy998.ui.activity.main.home.HomeFragment;
import com.yjy998.ui.activity.main.more.MoreFragment;
import com.yjy998.ui.activity.main.my.CenterFragment;
import com.yjy998.ui.view.TabItem;

import static com.yjy998.ui.view.TabItem.CheckListener;


public class MainActivity extends MenuActivity implements HomeFragment.HomeListener {

    public static final String EXTRA_CHECK_TAB_ID = "EXTRA_CHECK_TAB_ID";
    public static final int ID_HOME = R.id.tabHome;
    public static final int ID_GAME = R.id.tabGame;
    public static final int ID_APPLY = R.id.tabApply;
    public static final int ID_CENTER = R.id.tabCenter;
    public static final int ID_MORE = R.id.tabMore;
    //当前选中的tab
    private TabItem curTab;
    //首页
    HomeFragment mHome;
    //我的大赛
    ContestFragment2 mGameFragment;
    //实盘申请
    ApplyFragment mApplyFragment;
    //个人中心
    CenterFragment mCenterFragment;
    //更多
    MoreFragment mMoreFragment;
    //正在显示的页面
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
//        XiaomiUpdateAgent.update(this);
//        XiaomiUpdateAgent.setUpdateListener(new XiaomiUpdateListener() {
//            @Override
//            public void onUpdateReturned(int i, UpdateResponse updateResponse) {
//                SLog.debug_format("i:%d",i);
//
//            }
//        });
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
        VersionUtil.start(this, false,false);
    }


    private CheckListener listener = new CheckListener() {
        @Override
        public void onSelected(TabItem view) {

            if (curTab != null && curTab != view) {
                curTab.setChecked(false);
            }
            curTab = view;
            BaseFragment fragment = null;
            //采用延迟创建对象，来提高首次进入的速度
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
                        mGameFragment = new ContestFragment2();
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
            //如果当前页面已经在展示了，就不要再切换了
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

    /**
     * 刷新页面
     */
    @Override
    public void refreshLayout() {
        mMenuFragment.refresh();
        if (mCenterFragment != null && mCenterFragment.getView() != null) {
            mCenterFragment.refresh();
        }
    }
}
