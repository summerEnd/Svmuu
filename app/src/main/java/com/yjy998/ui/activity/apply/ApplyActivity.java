package com.yjy998.ui.activity.apply;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sp.lib.widget.PagerSlidingTabStrip;
import com.yjy998.R;
import com.yjy998.adapter.FragmentPagerAdapter;
import com.yjy998.ui.activity.other.MenuActivity;

public class ApplyActivity extends MenuActivity {

    private PagerSlidingTabStrip tabStrip;
    private ViewPager pager;
    public static final String _TN = "tn";
    public static final String _T9 = "t9";
    public static final String EXTRA_APPLY = "t9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tab_pager);
        initialize();
    }

    private void initialize() {

        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabStrip);
        pager = (ViewPager) findViewById(R.id.pager);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager());
        adapter.add(new TNFragment());
        adapter.add(new T9Fragment());
        pager.setAdapter(adapter);
        tabStrip.setViewPager(pager);

        if (_T9.equals(getIntent().getStringExtra(EXTRA_APPLY))) {
            pager.setCurrentItem(1);
        }
    }

    @Override
    public boolean onMenuClick(View v) {
        switch (v.getId()) {
            case R.id.applyT9: {
                pager.setCurrentItem(1);
                break;
            }
            case R.id.applyTn: {
                pager.setCurrentItem(0);
                break;
            }
            default:
                return false;
        }
        close();
        return true;
    }
}
