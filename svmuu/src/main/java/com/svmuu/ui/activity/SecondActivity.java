package com.svmuu.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.svmuu.R;
import com.svmuu.ui.BaseActivity;

/**
 * 二级页面
 */
public class SecondActivity extends BaseActivity {
    ViewGroup content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.second_layout);
        content= (ViewGroup) findViewById(R.id.content);
        initTitle();
    }

    private void initTitle() {
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void setContentView(int layoutResID) {
       setContentView(getLayoutInflater().inflate(layoutResID, content, false));
    }

    @Override
    public void setContentView(View view) {
        content.addView(view);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.back){
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                break;
            }
        }

        return true;
    }
}
