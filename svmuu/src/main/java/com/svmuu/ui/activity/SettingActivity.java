package com.svmuu.ui.activity;

import android.content.pm.PackageInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.VersionUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sp.lib.common.util.ContextUtil;
import com.svmuu.R;
import com.svmuu.ui.BaseActivity;

public class SettingActivity extends SecondActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        TextView tv = (TextView) findViewById(R.id.tv_version);
        PackageInfo packageInfo = ContextUtil.getPackageInfo();
        if (packageInfo == null) {
            tv.setText(getString(R.string.version_s, "0"));
        } else {
            tv.setText(getString(R.string.version_s, packageInfo.versionName));
        }
        findViewById(R.id.aboutUs).setOnClickListener(this);
        findViewById(R.id.suggestion).setOnClickListener(this);
        findViewById(R.id.clear).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.aboutUs:{
                break;
            }
            case R.id.suggestion:{
                break;
            }
            case R.id.clear:{
                break;
            }
            case R.id.logout:{
                break;
            }
        }
        super.onClick(v);
    }
}
