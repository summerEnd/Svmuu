package com.sp.lib.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.sp.lib.support.IntentFactory;

import java.util.List;

import static android.app.ActivityManager.RunningAppProcessInfo;


public class SlibActivity extends Activity {

    private boolean showLifeCircle = false;

    public <T> T findView(int id) {
        return (T) findViewById(id);
    }

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

}
