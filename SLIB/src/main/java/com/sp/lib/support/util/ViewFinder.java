package com.sp.lib.support.util;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;

import static android.view.View.OnClickListener;

public class ViewFinder {

    private View     mView;

    public ViewFinder(View view) {
        this.mView = view;
    }

    public <T extends View> T findView(int id) {
        return (T) mView.findViewById(id);
    }

    public <T extends View> T setOnClick(int id, OnClickListener l) {
        T view = (T) mView.findViewById(id);
        view.setOnClickListener(l);
        return view;
    }

    public static <T> T findView(View v, int id) {
        return (T) v.findViewById(id);
    }

}
