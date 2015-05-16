package com.yjy998.ui.activity.main.home;

import android.os.Bundle;
import android.view.View;

import com.yjy998.R;
import com.yjy998.ui.activity.base.SecondActivity;

public class NewMemberActivity extends SecondActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresh);
        findViewById(R.id.expNow).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expNow: {

                break;
            }
        }
    }
}
