package com.svmuu.ui.activity;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.svmuu.R;
import com.svmuu.ui.BaseActivity;

public class LiveListActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{
    RadioGroup group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_list);
        group= (RadioGroup) findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer,new LiveFragment()).commit();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
    }
}
