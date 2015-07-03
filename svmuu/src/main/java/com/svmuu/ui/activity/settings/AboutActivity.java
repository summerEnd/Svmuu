package com.svmuu.ui.activity.settings;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.svmuu.R;
import com.svmuu.ui.BaseActivity;
import com.svmuu.ui.activity.SecondActivity;

public class AboutActivity extends SecondActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

}
