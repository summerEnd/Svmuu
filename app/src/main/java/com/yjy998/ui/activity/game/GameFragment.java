package com.yjy998.ui.activity.game;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sp.lib.common.util.ViewFinder;
import com.sp.lib.widget.PagerSlidingTabStrip;
import com.yjy998.R;
import com.yjy998.adapter.FragmentPagerAdapter;
import com.yjy998.ui.activity.other.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class GameFragment extends BaseFragment {


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
        initialize(layout);
        return layout;
    }


    private void initialize(View v) {
        ViewFinder layout = new ViewFinder(v);
        tabStrip = layout.findView(R.id.tabStrip);
        pager = layout.findView(R.id.pager);
        pager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(), fragments));
        tabStrip.setViewPager(pager);
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
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
