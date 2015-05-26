package com.slib.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sp.lib.ToggleView;
import com.sp.lib.widget.slide.menu.MenuDrawer;
import com.sp.lib.widget.slide.menu.Position;

public class ToggleTest extends SLIBTest {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final MenuDrawer mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.START, MenuDrawer.MENU_DRAG_CONTENT);


        LinearLayout contentView = new LinearLayout(this);
        final ToggleView child = new ToggleView(this);
        contentView.addView(child,new LinearLayout.LayoutParams(80,80));
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setBackgroundColor(Color.GRAY);
        mMenuDrawer.setMenuView(frameLayout);
        mMenuDrawer.setMenuSize(300);
        mMenuDrawer.setContentView(contentView);
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMenuDrawer.isMenuVisible()){
                    mMenuDrawer.closeMenu();
                }else{
                    mMenuDrawer.openMenu();
                }
            }
        });

        mMenuDrawer.setOnDrawerStateChangeListener(new MenuDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {

            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                child.setRatio(openRatio);
            }
        });
    }
}
