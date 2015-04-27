package com.sp.lib.demo;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public abstract class SlibDemoWrapper {
    private SlibDemo demo;
    private String label;
    private String description;

    protected SlibDemoWrapper(SlibDemo demo) {
        this(demo,null,null);
    }

    public SlibDemoWrapper(SlibDemo demo, String label, String description) {
        this.demo = demo;
        this.label = label;
        this.description = description;
    }

    public void setContentView(int layoutResID) {
        demo.setContentView(layoutResID);
    }

    public void setContentView(View view) {
        demo.setContentView(view);
    }

    public View findViewById(int id) {
        return demo.findViewById(id);
    }

    public final String getTestLabel() {
        return label;
    }

    public final String getTestDescription() {
        return description;
    }

    protected abstract void onCreate();

    public SlibDemo getActivity() {
        return demo;
    }

    /**
     * @return true拦截返回事件。
     */
    public boolean onBackPressed() {
        return false;
    }

    //生命周期
    protected void onStart() {
    }

    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return demo.registerReceiver(receiver, filter);
    }

    public void unregisterReceiver(BroadcastReceiver receiver) {
        demo.unregisterReceiver(receiver);
    }

    public ContentResolver getContentResolver() {
        return demo.getContentResolver();
    }

    public void startActivity(Intent intent) {
        demo.startActivity(intent);
        getActivity().mStacks.pop();
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        demo.startActivityForResult(intent, requestCode);
    }

    public LayoutInflater getLayoutInflater() {
        return demo.getLayoutInflater();
    }

    public MenuInflater getMenuInflater() {
        return demo.getMenuInflater();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }


    protected void onRestart() {
    }

    protected void onResume() {
    }

    protected void onPause() {
    }

    protected void onStop() {
    }


    protected void onDestroyed() {
    }
}
