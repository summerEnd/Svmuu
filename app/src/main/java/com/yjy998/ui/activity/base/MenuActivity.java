package com.yjy998.ui.activity.base;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.sp.lib.common.support.IntentFactory;
import com.sp.lib.widget.slide.menu.MenuDrawer;
import com.sp.lib.widget.slide.menu.Position;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.ui.activity.main.MenuFragment;
import com.yjy998.ui.activity.admin.LoginRegisterWindow;

public class MenuActivity extends YJYActivity implements MenuFragment.OnMenuClick {
    private ViewGroup layoutContainer;
    protected MenuFragment mMenuFragment;
    private MenuDrawer mMenuDrawer;
    private ImageView titleImage;
    private LoginRegisterWindow mLoginWindow;
    /**
     * 标志登录注册窗口是否为第一次弹出
     */
    private static boolean isFirstShow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        if (getActionBar() != null) getActionBar().hide();

        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.START, MenuDrawer.MENU_DRAG_CONTENT);

        View contentView = getLayoutInflater().inflate(R.layout.menu_content, null);
        layoutContainer = (ViewGroup) contentView.findViewById(R.id.layoutContainer);
        mMenuFragment = new MenuFragment();
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(R.id.menuContainer);
        mMenuDrawer.setMenuView(frameLayout);
        mMenuDrawer.setMenuSize(getResources().getDimensionPixelSize(R.dimen.menuWidth));
        mMenuDrawer.setContentView(contentView);

        getSupportFragmentManager().beginTransaction().add(R.id.menuContainer, mMenuFragment).commit();
        titleImage = (ImageView) findViewById(R.id.titleImage);
        titleImage.setOnClickListener(titleClickListener);
        findViewById(R.id.toggle).setOnClickListener(titleClickListener);
    }

    /**
     * 标题栏点击事件
     */
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
                        startActivity(IntentFactory.callPhone(getString(R.string.service_tel)));
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
        if (isFirstShow) {
            showLoginWindow();
        }
    }

    /**
     * 弹出登录注册window
     */
    public void showLoginWindow() {

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
        isFirstShow = false;
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
    public void setContentView(View view) {
        layoutContainer.removeAllViews();
        layoutContainer.addView(view);
    }

    public void toggle() {
        if (mMenuDrawer.isMenuVisible()) {
            close();
        } else {
            open();

        }
    }

    public void open() {
        mMenuFragment.refresh();
        mMenuDrawer.openMenu();

    }

    /**
     * 关闭当前menu
     */
    public void close() {
        mMenuDrawer.closeMenu();
    }

    @Override
    public void onBackPressed() {
        if (mMenuDrawer.isMenuVisible()) {
            close();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 如果要拦截侧边栏的点击事件，就重写此方法
     *
     * @return true 拦截，false 不拦截
     */
    @Override
    public boolean onMenuClick(View v) {
        return false;
    }
}
