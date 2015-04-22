package com.yjy998.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.ui.activity.apply.ApplyFragment;
import com.yjy998.ui.activity.game.GameFragment;
import com.yjy998.ui.activity.home.HomeFragment;
import com.yjy998.ui.activity.home.HomeLoginFragment;
import com.yjy998.ui.activity.more.MoreFragment;
import com.yjy998.ui.activity.my.CenterFragment;
import com.yjy998.ui.pop.LoginRegisterWindow;
import com.yjy998.ui.view.TabItem;

import static com.yjy998.ui.view.TabItem.CheckListener;


public class MainActivity extends MenuActivity implements HomeFragment.HomeListener {
    private TabItem curTab;
    HomeFragment mHome;
    HomeLoginFragment mHomeLogin;
    GameFragment mGameFragment;
    ApplyFragment mApplyFragment;
    CenterFragment mCenterFragment;
    MoreFragment mMoreFragment;
    Fragment displayingFragment;
    TabItem tabHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleImage: {

                break;
            }
            case R.id.toggle: {
                toggle();
                break;
            }

        }
    }

    private void initialize() {

        TabItem tabReal = (TabItem) findViewById(R.id.tabReal);
        TabItem tabMore = (TabItem) findViewById(R.id.tabMore);
        TabItem tabApply = (TabItem) findViewById(R.id.tabApply);
        tabHome = (TabItem) findViewById(R.id.tabHome);
        TabItem tabCenter = (TabItem) findViewById(R.id.tabCenter);

        tabReal.setCheckListener(listener);
        tabMore.setCheckListener(listener);
        tabHome.setCheckListener(listener);
        tabApply.setCheckListener(listener);
        tabCenter.setCheckListener(listener);

        tabHome.performClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshHomeFragment();
    }

    @Override
    protected void refreshLayout() {
        super.refreshLayout();
        refreshHomeFragment();
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
                    boolean isLogin = AppDelegate.getInstance().isUserLogin();
                    if (isLogin) {
                        if (mHomeLogin == null) {
                            mHomeLogin = new HomeLoginFragment();

                        }
                        fragment = mHomeLogin;

                    } else {

                        if (mHome == null) {
                            mHome = new HomeFragment();

                        }
                        fragment = mHome;
                    }

                    break;
                }
                case R.id.tabApply: {
                    if (mApplyFragment == null) {
                        mApplyFragment = new ApplyFragment();
                    }
                    fragment = mApplyFragment;
                    break;
                }
                case R.id.tabReal: {
                    if (mGameFragment == null) {
                        mGameFragment = new GameFragment();
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
                findViewById(R.id.tabCenter).performClick();
                break;
            }
            case R.id.realGame: {
                findViewById(R.id.tabReal).performClick();
                break;
            }

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mCenterFragment != null && mCenterFragment.dispatchTouch(ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    void refreshHomeFragment() {
        boolean isLogin = AppDelegate.getInstance().isUserLogin();
        if ((isLogin && displayingFragment == mHome) ||
                (!isLogin && displayingFragment == mHomeLogin)) {
            //刷新首页Fragment
            listener.onSelected(tabHome);
        }
    }
}
