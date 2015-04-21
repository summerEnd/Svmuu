package com.yjy998.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.Gravity;
import android.view.View;

import com.yjy998.R;
import com.yjy998.ui.activity.apply.ApplyFragment;
import com.yjy998.ui.activity.game.GameFragment;
import com.yjy998.ui.activity.home.HomeFragment;
import com.yjy998.ui.activity.more.MoreFragment;
import com.yjy998.ui.activity.my.CenterFragment;
import com.yjy998.ui.pop.LoginRegisterWindow;
import com.yjy998.ui.view.TabItem;

import static com.yjy998.ui.view.TabItem.CheckListener;


public class MainActivity extends YJYActivity {
    MenuFragment mMenuFragment;
    LoginRegisterWindow mLoginWindow;
    private SlidingPaneLayout slidingPane;
    private TabItem curTab;
    HomeFragment mHomeFragment;
    GameFragment mGameFragment;
    ApplyFragment mApplyFragment;
    CenterFragment mCenterFragment;
    MoreFragment mMoreFragment;
    Fragment displayingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_main);
        mMenuFragment = new MenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.menuContainer, mMenuFragment).commit();
        mLoginWindow = new LoginRegisterWindow(this);
        initialize();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.centerImage: {
                mLoginWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
            }
            case R.id.toggle: {
                if (slidingPane.isOpen()) {
                    slidingPane.closePane();
                } else {
                    slidingPane.openPane();
                }
                break;
            }
        }
    }

    private void initialize() {

        TabItem tabReal = (TabItem) findViewById(R.id.tabReal);
        TabItem tabMore = (TabItem) findViewById(R.id.tabMore);
        TabItem tabApply = (TabItem) findViewById(R.id.tabApply);
        TabItem tabHome = (TabItem) findViewById(R.id.tabHome);
        TabItem tabCenter = (TabItem) findViewById(R.id.tabCenter);
        findViewById(R.id.toggle).setOnClickListener(this);
        findViewById(R.id.centerImage).setOnClickListener(this);

        slidingPane = (SlidingPaneLayout) findViewById(R.id.slidingPane);
        slidingPane.setParallaxDistance(50);
        slidingPane.setCoveredFadeColor(getResources().getColor(R.color.transientBlack));
        slidingPane.setSliderFadeColor(0);

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

            if (curTab != null) {
                curTab.setChecked(false);
            }
            curTab = view;
            BaseFragment fragment = null;
            switch (view.getId()) {
                case R.id.tabHome: {
                    if (mHomeFragment == null) {
                        mHomeFragment = new HomeFragment();
                    }
                    fragment = mHomeFragment;
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
            if (fragment != null) {
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
}
