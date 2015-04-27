package com.yjy998.ui.activity.test;

import android.os.Bundle;

import com.sp.lib.widget.slide.menu.MenuDrawer;
import com.sp.lib.widget.slide.menu.Position;
import com.yjy998.R;
import com.yjy998.ui.activity.YJYActivity;

/**
 * Created by user1 on 2015/4/27.
 */
public class MenuDrawerActivity extends YJYActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MenuDrawer drawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.START, MenuDrawer.MENU_DRAG_CONTENT);
        drawer.setMenuView(R.layout.fragment_main_menu);
        setContentView(drawer);
        drawer.setContentView(R.layout.activity_main);
    }
}
