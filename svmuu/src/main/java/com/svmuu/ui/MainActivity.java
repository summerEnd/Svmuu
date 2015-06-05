package com.svmuu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sp.lib.activity.DEBUGActivity;
import com.sp.lib.activity.SlibActivity;
import com.sp.lib.common.util.ShortCut;
import com.sp.lib.widget.nav.SimpleTab;
import com.sp.lib.widget.nav.TabBar;
import com.svmuu.R;


public class MainActivity extends BaseActivity implements TabBar.OnTabSelectListener {
    TabBar tabBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tabBar = (TabBar) findViewById(R.id.tabBar);
        tabBar.setOnTabSelectListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, DEBUGActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSelect(SimpleTab tab) {
        switch (tab.getId()) {
            case R.id.tb_home:
                break;
            case R.id.tb_live:
                break;
            case R.id.tb_article:
                break;
            case R.id.tb_video:
                break;
            case R.id.tb_contest:
                break;
        }
    }
}
