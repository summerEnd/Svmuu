package com.yjy998.ui.activity.other;

import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.sp.lib.common.support.IntentFactory;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.ui.activity.YJYActivity;
import com.yjy998.ui.pop.LoginRegisterWindow;

public class MenuActivity extends YJYActivity {
    private ViewGroup layoutContainer;
    private MenuFragment mMenuFragment;
    private SlidingPaneLayout slidingPane;
    private ImageView titleImage;
    private LoginRegisterWindow mLoginWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_menu);
        //隐藏标题栏
        getActionBar().hide();
        layoutContainer = (ViewGroup) findViewById(R.id.layoutContainer);
        mMenuFragment = new MenuFragment();

        slidingPane = (SlidingPaneLayout) findViewById(R.id.slidingPane);
        slidingPane.setParallaxDistance(50);
        slidingPane.setCoveredFadeColor(getResources().getColor(R.color.transientBlack));
        slidingPane.setSliderFadeColor(0);
        getSupportFragmentManager().beginTransaction().add(R.id.menuContainer, mMenuFragment).commit();
        titleImage = (ImageView) findViewById(R.id.titleImage);
        titleImage.setOnClickListener(titleClickListener);
        findViewById(R.id.toggle).setOnClickListener(titleClickListener);
    }

    private View.OnClickListener titleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.toggle: {
                    toggle();
                    break;
                }
                case R.id.titleImage: {
                    //登陆就打电话，没登录就跳到登陆。这是MenuActivity通用的。
                    if (AppDelegate.getInstance().isUserLogin()) {
                        startActivity(IntentFactory.callPhone("138465688"));
                    } else {
                        showLoginWindow();
                    }

                    break;
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        refreshTitle();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        showLoginWindow();
    }

    private void showLoginWindow() {

        if (AppDelegate.getInstance().isUserLogin()) {
            return;
        }

        if (mLoginWindow == null) {
            mLoginWindow = new LoginRegisterWindow(MenuActivity.this);
            mLoginWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    refreshTitle();
                    refreshLayout();
                }
            });
        }
        mLoginWindow.showAtLocation(titleImage, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 刷新右上角图标状态
     */
    private void refreshTitle() {
        if (AppDelegate.getInstance().isUserLogin()) {
            setTitleImage(R.drawable.ic_call);
        } else {
            setTitleImage(R.drawable.nav_center);
        }
    }

    /**
     * 登陆状态发生改变时要刷新一下布局
     */
    protected void refreshLayout() {

    }

    /**
     * 设置右上角图标
     */
    protected final void setTitleImage(int resId) {
        titleImage.setImageResource(resId);
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(getLayoutInflater().inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {

    }

    @Override
    public void setContentView(View view) {
        layoutContainer.removeAllViews();
        layoutContainer.addView(view);
    }

    public void toggle() {
        if (slidingPane.isOpen()) {
            slidingPane.closePane();
        } else {
            slidingPane.openPane();
        }
    }

    @Override
    public void onBackPressed() {
        if (slidingPane.isOpen()) {
            slidingPane.closePane();
        } else {
            super.onBackPressed();
        }
    }
}
