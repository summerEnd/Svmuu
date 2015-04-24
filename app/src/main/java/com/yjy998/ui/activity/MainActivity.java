package com.yjy998.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;

import com.yjy998.R;
import com.yjy998.ui.activity.apply.ApplyFragment;
import com.yjy998.ui.activity.game.GameFragment;
import com.yjy998.ui.activity.home.HomeFragment;
import com.yjy998.ui.activity.more.MoreFragment;
import com.yjy998.ui.activity.my.CenterFragment;
import com.yjy998.ui.activity.other.BaseFragment;
import com.yjy998.ui.activity.other.MenuActivity;
import com.yjy998.ui.view.TabItem;

import static com.yjy998.ui.view.TabItem.CheckListener;


public class MainActivity extends MenuActivity implements HomeFragment.HomeListener {
    private TabItem curTab;
    HomeFragment mHome;
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
        if (mGameFragment != null && mGameFragment.dispatchTouch(ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

}
