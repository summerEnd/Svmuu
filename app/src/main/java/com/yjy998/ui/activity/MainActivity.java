package com.yjy998.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.yjy998.R;
import com.yjy998.ui.pop.LoginRegisterWindow;
import com.yjy998.ui.view.TabItem;

import static com.yjy998.ui.view.TabItem.CheckListener;


public class MainActivity extends YJYActivity {
    MenuFragment mMenuFragment;
    LoginRegisterWindow mLoginWindow;
    private ImageView centerImage;
    private ImageView toggle;
    private FrameLayout menuContainer;
    private TabItem tabReal;
    private TabItem tabMore;
    private TabItem tabApply;
    private TabItem tabHome;
    private TabItem tabCenter;
    private FrameLayout fragmentContainer;
    private SlidingPaneLayout slidingPane;
    private TabItem curTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_main);


        mMenuFragment = new MenuFragment();
        getFragmentManager().beginTransaction().add(R.id.menuContainer, mMenuFragment).commit();
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

        centerImage = (ImageView) findViewById(R.id.centerImage);
        toggle = (ImageView) findViewById(R.id.toggle);
        menuContainer = (FrameLayout) findViewById(R.id.menuContainer);
        tabReal = (TabItem) findViewById(R.id.tabReal);
        tabMore = (TabItem) findViewById(R.id.tabMore);
        tabApply = (TabItem) findViewById(R.id.tabApply);
        tabHome = (TabItem) findViewById(R.id.tabHome);
        tabCenter = (TabItem) findViewById(R.id.tabCenter);

        fragmentContainer = (FrameLayout) findViewById(R.id.fragmentContainer);
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
            switch (view.getId()) {
                case R.id.tabHome: {
                    break;
                }
                case R.id.tabApply: {
                    break;
                }
                case R.id.tabReal: {
                    break;
                }
                case R.id.tabCenter: {
                    break;
                }
                case R.id.tabMore: {
                    break;
                }
            }

        }
    };
}
