package com.yjy998.ui.activity;

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
        layoutContainer = (ViewGroup) findViewById(R.id.layoutContainer);
        mMenuFragment = new MenuFragment();

        slidingPane = (SlidingPaneLayout) findViewById(R.id.slidingPane);
        slidingPane.setParallaxDistance(50);
        slidingPane.setCoveredFadeColor(getResources().getColor(R.color.transientBlack));
        slidingPane.setSliderFadeColor(0);
        getSupportFragmentManager().beginTransaction().add(R.id.menuContainer, mMenuFragment).commit();
        titleImage = (ImageView) findViewById(R.id.titleImage);
        titleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登陆就打电话，没登录就跳到登陆。这是MenuActivity通用的。
                if (AppDelegate.getInstance().isUserLogin()) {
                    startActivity(IntentFactory.callPhone("138465688"));
                } else {
                    if (mLoginWindow == null) {
                        mLoginWindow = new LoginRegisterWindow(MenuActivity.this);
                        mLoginWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                refreshLayout();
                            }
                        });
                    }
                    mLoginWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                }

                onTitleImageClick();
            }
        });
        refreshLayout();
    }

    /**
     * 刷新右上角图标状态,登陆状态发生改变时要刷新一下布局
     */
    protected void refreshLayout() {
        if (AppDelegate.getInstance().isUserLogin()) {
            setTitleImage(R.drawable.ic_call);
        } else {
            setTitleImage(R.drawable.nav_center);
        }
    }

    /**
     * 标题栏右上角图标被点击时
     */
    protected final void onTitleImageClick() {

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
}
