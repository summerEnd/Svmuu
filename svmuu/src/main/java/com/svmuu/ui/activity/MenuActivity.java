package com.svmuu.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sp.lib.widget.slide.menu.MenuDrawer;
import com.sp.lib.widget.slide.menu.Position;
import com.sp.lib.widget.slide.toggle.ArrowToggle;
import com.svmuu.AppDelegate;
import com.svmuu.R;
import com.svmuu.ui.BaseActivity;
import com.svmuu.ui.pop.LoginDialog;

public class MenuActivity extends BaseActivity{
    private ViewGroup layoutContainer;
    protected MenuFragment mMenuFragment;
    private MenuDrawer mMenuDrawer;

    ArrowToggle toggle;
    View signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.START, MenuDrawer.MENU_DRAG_WINDOW);

        View contentView = getLayoutInflater().inflate(R.layout.menu_content, null);
        layoutContainer = (ViewGroup) contentView.findViewById(R.id.layoutContainer);
        mMenuFragment = new MenuFragment();
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(R.id.menuContainer);
        mMenuDrawer.setMenuView(frameLayout);
        mMenuDrawer.setMenuSize(getResources().getDimensionPixelSize(R.dimen.menuWidth));
        mMenuDrawer.setContentView(contentView);
        getSupportFragmentManager().beginTransaction().add(R.id.menuContainer, mMenuFragment).commit();

        toggle = (ArrowToggle) findViewById(R.id.toggle);
        signIn = findViewById(R.id.signIn);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDialog loginDialog = new LoginDialog(MenuActivity.this);
                loginDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        onUserChanged();

                        initTitle();
                    }
                });
                loginDialog.show();
            }
        });
        mMenuDrawer.setOnDrawerStateChangeListener(new MenuDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {

            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                toggle.setRatio(openRatio);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTitle();
    }

    private void initTitle() {
        if (AppDelegate.getInstance().isLogin()) {
            signIn.setVisibility(View.INVISIBLE);
        } else {
            signIn.setVisibility(View.VISIBLE);
        }
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
        mMenuFragment.requestRefresh();
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

}
