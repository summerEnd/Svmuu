package com.svmuu.ui.activity;

import android.app.Activity;
import android.view.View;

import com.svmuu.ui.BaseFragment;

public class MenuFragment extends BaseFragment implements View.OnClickListener {


    private OnMenuClick menuClick;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnMenuClick) {
            menuClick = (OnMenuClick) activity;
        }
    }



    @Override
    public void onClick(View v) {

    }



    public interface OnMenuClick {

         boolean onMenuClick(View v);
    }
}
