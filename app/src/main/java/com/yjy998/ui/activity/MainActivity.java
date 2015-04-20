package com.yjy998.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;

import com.yjy998.R;
import com.yjy998.ui.dialog.RegisterDialog;


public class MainActivity extends YJYActivity {
    SlidingPaneLayout mSlideLayout;
    MenuFragment mMenuFragment;
    RegisterDialog mRegisterDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(com.yjy998.R.layout.activity_main);
        mSlideLayout = (SlidingPaneLayout) findViewById(R.id.slidingPane);
        mSlideLayout.setParallaxDistance(50);
        mSlideLayout.setCoveredFadeColor(getResources().getColor(R.color.transientBlack));
        mSlideLayout.setSliderFadeColor(0);
        mMenuFragment = new MenuFragment();
        getFragmentManager().beginTransaction().add(R.id.menuContainer, mMenuFragment).commit();
        findViewById(R.id.toggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSlideLayout.isOpen()) {
                    mSlideLayout.closePane();
                } else {
                    mSlideLayout.openPane();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login: {
                if (mRegisterDialog == null) {
                    mRegisterDialog = new RegisterDialog(this);
                }
                mRegisterDialog.show();
                break;
            }
        }
    }
}
