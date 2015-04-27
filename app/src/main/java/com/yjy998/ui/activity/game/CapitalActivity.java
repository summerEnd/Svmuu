package com.yjy998.ui.activity.game;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.yjy998.R;
import com.yjy998.ui.activity.YJYActivity;
import com.yjy998.ui.activity.other.SecondActivity;

public class CapitalActivity extends SecondActivity {
    CapitalFragment mFragment = new CapitalFragment();
    public static final String EXTRA_IS_BUY = "extra_buy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(R.id.fragmentContainer);
        setContentView(frameLayout);
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, mFragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stand_still, R.anim.slide_down_out);
    }
}