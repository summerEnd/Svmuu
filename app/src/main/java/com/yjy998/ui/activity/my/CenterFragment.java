package com.yjy998.ui.activity.my;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sp.lib.widget.PagerSlidingTabStrip;
import com.yjy998.R;
import com.yjy998.ui.activity.BaseFragment;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.yjy998.ui.activity.my.CenterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CenterFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    View layout;
    private PagerSlidingTabStrip tabStrip;
    private ViewPager pager;
    BaseFragment[] fragments = new BaseFragment[]{
            new CapitalFragment(),
            new HoldingsFragment(),
            new CancellationEntrustFragment(),
            new DealFragment()
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_center, container, false);
        initialize();
        return layout;
    }


    private void initialize() {

        tabStrip = (PagerSlidingTabStrip) layout.findViewById(R.id.tabStrip);
        pager = (ViewPager) layout.findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        tabStrip.setViewPager(pager);
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position].getTitle();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }

    //如果要实现ViewPager的滑动，就把注释去掉
    public boolean dispatchTouch(MotionEvent event) {

//        if (!isVisible()) {
//            return false;
//        }
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                pager.dispatchTouchEvent(event);
//                break;
//            }
//            case MotionEvent.ACTION_MOVE: {
//                if (pager.canScrollHorizontally(-1)) {
//                    return pager.dispatchTouchEvent(event);
//                } else if (pager.canScrollHorizontally(1)) {
//                    return pager.dispatchTouchEvent(event);
//                }
//                break;
//            }
//        }

        return false;
    }

}
