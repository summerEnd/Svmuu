package com.sp.lib.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.sp.lib.common.interfaces.TouchDispatcher;
import com.sp.lib.common.interfaces.TouchObserver;

import java.util.ArrayList;
import java.util.List;


public class SlibActivity extends FragmentActivity implements TouchDispatcher {

    private boolean showLifeCircle = false;

    public <T> T findView(int id) {
        return (T) findViewById(id);
    }

    private List<TouchObserver> mToucheDispatchers = new ArrayList<TouchObserver>();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setTitle(getTitle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        debug("onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        debug("onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        debug("onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        debug("onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        debug("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        debug("onDestroy");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        debug("onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        debug("onSaveInstanceState");
    }

    void debug(String msg) {
        if (showLifeCircle) Log.i(getClass().getSimpleName(), msg);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {


        for (TouchObserver observer : mToucheDispatchers) {
            if (!observer.isEnabled()) {
                continue;
            }
            if (observer.dispatch(ev)) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void register(TouchObserver dispatcher) {
        if (mToucheDispatchers.contains(dispatcher)) {
            return;
        }
        mToucheDispatchers.add(dispatcher);
    }

    @Override
    public void unRegister(TouchObserver dispatcher) {
        mToucheDispatchers.remove(dispatcher);
    }
}
