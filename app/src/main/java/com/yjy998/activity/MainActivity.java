package com.yjy998.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.yjy998.R;


public class MainActivity extends YJYActivity {
    SlidingPaneLayout mSlideLayout;
    MenuFragment mMenuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.yjy998.R.layout.activity_main);
        mSlideLayout = (SlidingPaneLayout) findViewById(R.id.slidingPane);
        mMenuFragment = new MenuFragment();
        getFragmentManager().beginTransaction().add(R.id.menuContainer, mMenuFragment).commit();
        findViewById(R.id.toggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), ChartActivity.class));
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


}
