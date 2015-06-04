package com.sp.lib.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Stack;

public class SlibDemo extends Activity implements AdapterView.OnItemClickListener {

    ArrayList<SlibDemoWrapper> mTests = new ArrayList<SlibDemoWrapper>();
    private final String EXTRA_TEST = "activity_test";
    Stack<SlibDemoWrapper> mStacks = new Stack<SlibDemoWrapper>();
    private Menu mMenu;
    private static Class<? extends Activity> main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testList();
    }

    public static void setMainTest(Class<? extends Activity> activity) {
        main = activity;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    private void testList() {
        if (mTests.size() == 0) {
            // Add Test here
            if (main != null) {
                mTests.add(new MainTest(this, main));
            }
            mTests.add(new DownloadTest(this));
            mTests.add(new SlidingTest(this));
        }
        ListView listView = new ListView(this);
        setContentView(listView);
        listView.setOnItemClickListener(this);
        listView.setAdapter(new DemoAdapter());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        int ACTIVITY = intent.getIntExtra(EXTRA_TEST, 0);
        SlibDemoWrapper wrapper = mTests.get(ACTIVITY);
        mStacks.push(wrapper);
        wrapper.onCreate();
        wrapper.onCreateOptionsMenu(mMenu);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        startActivity(new Intent(this, SlibDemo.class).putExtra(EXTRA_TEST, position));
    }

    @Override
    public void onBackPressed() {

        if (mStacks.isEmpty()) {
            super.onBackPressed();
            return;
        }
        //向测试分发返回键
        if (mStacks.peek().onBackPressed()) {
            return;
        }

        mStacks.pop();

        int size = mStacks.size();

        if (size == 0) {
            testList();
        } else {
            SlibDemoWrapper wrapper = mStacks.peek();
            wrapper.onCreate();
        }
    }

    private class DemoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTests.size();
        }

        @Override
        public Object getItem(int position) {
            return mTests.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(android.R.layout.simple_expandable_list_item_2, null);
            }
            SlibDemoWrapper wrapper = mTests.get(position);
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(wrapper.getTestLabel());
            ((TextView) convertView.findViewById(android.R.id.text2)).setText(wrapper.getTestDescription());
            return convertView;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mStacks.size() != 0) {
            mStacks.peek().onStart();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mStacks.size() != 0) {
            mStacks.peek().onRestart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mStacks.size() != 0) {
            mStacks.peek().onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mStacks.size() != 0) {
            mStacks.peek().onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mStacks.size() != 0) {
            mStacks.peek().onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mStacks.size() != 0) {
            mStacks.peek().onDestroyed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mStacks.size() != 0 && mStacks.peek().onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return mStacks.size() != 0 && mStacks.peek().onPrepareOptionsMenu(menu) || super.onPrepareOptionsMenu(menu);
    }
}
