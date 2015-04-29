package com.yjy998.ui.activity.other.pay;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.widget.PagerSlidingTabStrip;
import com.yjy998.R;
import com.yjy998.adapter.FragmentPagerAdapter;
import com.yjy998.ui.activity.apply.T9Fragment;
import com.yjy998.ui.activity.apply.TNFragment;
import com.yjy998.ui.activity.other.SecondActivity;

public class RechargeActivity extends SecondActivity {

    private PagerSlidingTabStrip tabStrip;
    private ViewPager pager;
    private int number;
    int SELECTED_COLOR;
    int NORMAL_COLOR;
    ViewGroup current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tab_pager);
        initialize();
    }

    @SuppressWarnings("ResourceType")
    private void initialize() {

        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabStrip);
        pager = (ViewPager) findViewById(R.id.pager);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager());
        adapter.add(new YbPayFragment());
        adapter.add(new BankFragment());
        pager.setAdapter(adapter);
        tabStrip.setViewPager(pager);

        SELECTED_COLOR = Color.parseColor("#F9F9F9");
        NORMAL_COLOR = Color.WHITE;
    }

    /**
     * 当易宝数量被点击时，会自动调用这个方法。
     *
     * @param v
     */
    public void onYbClick(View v) {

        if (current != null) {
            int blue = getResources().getColor(R.color.deepBlue);
            current.setBackgroundColor(NORMAL_COLOR);
            ((TextView) current.getChildAt(0)).setTextColor(blue);
            ((TextView) current.getChildAt(1)).setTextColor(blue);
        }

        v.setBackgroundColor(SELECTED_COLOR);
        current = (ViewGroup) v;
        int red = getResources().getColor(R.color.textColorRed);
        ((TextView) current.getChildAt(0)).setTextColor(red);
        ((TextView) current.getChildAt(1)).setTextColor(red);
    }

}
