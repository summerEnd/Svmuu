package com.yjy998.activity;

import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.yjy998.R;


public class MainActivity extends YJYActivity {
    SlidingPaneLayout mSlideLayout;
    MenuFragment mMenuFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.yjy998.R.layout.activity_main);
        mSlideLayout = (SlidingPaneLayout) findViewById(R.id.slidingPane);
        findViewById(R.id.toggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSlideLayout.isOpen()){
                    mSlideLayout.closePane();
                }else{
                    mSlideLayout.openPane();
                }
                if(mMenuFragment==null){
                    mMenuFragment=new MenuFragment();
                    getFragmentManager().beginTransaction().add(R.id.menuContainer, mMenuFragment).commit();
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.yjy998.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.yjy998.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
