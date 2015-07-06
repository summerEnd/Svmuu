package com.svmuu.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.svmuu.R;

/**
 * Created by Lincoln on 15/6/5.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.back){
            finish();
        }
    }
}
