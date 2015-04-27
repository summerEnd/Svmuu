package com.yjy998.ui.activity.other;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
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
import com.yjy998.ui.activity.YJYActivity;
import com.yjy998.ui.pop.LoginRegisterWindow;

public class MenuActivity extends YJYActivity implements MenuFragment.OnMenuClick {
    private ViewGroup layoutContainer;
    private MenuFragment mMenuFragment;
    private MenuDrawer mMenuDrawer;
    private ImageView titleImage;
    private LoginRegisterWindow mLoginWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        getActionBar().hide();
        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.START, MenuDrawer.MENU_DRAG_CONTENT);

        View contentView=getLayoutInflater().inflate(R.layout.menu_content,null);
        layoutContainer = (ViewGroup)contentView. findViewById(R.id.layoutContainer);
        mMenuFragment = new MenuFragment();
        FrameLayout frameLayout=new FrameLayout(this);
        frameLayout.setId(R.id.menuContainer);
        mMenuDrawer.setMenuView(frameLayout);
        mMenuDrawer.setContentView(contentView);

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
        if (mMenuDrawer.isMenuVisible()) {
            mMenuDrawer.closeMenu();
        } else {
            mMenuDrawer.openMenu();
        }
    }

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
        close();
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoginWindow != null) {
            mLoginWindow.dismiss();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
