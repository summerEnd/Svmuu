package com.svmuu.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Lincoln on 15/6/5.
 */
public class BaseActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
    }
}
