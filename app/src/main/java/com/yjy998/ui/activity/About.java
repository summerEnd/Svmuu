package com.yjy998.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sp.lib.common.support.update.UpdateHandler;
import com.sp.lib.common.support.update.UpdateManager;
import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;
import com.yjy998.ui.activity.other.SecondActivity;

import static android.view.View.OnClickListener;

public class About extends SecondActivity implements OnClickListener {

    private TextView curVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initialize();
    }

    private void initialize() {

        curVersion = (TextView) findViewById(R.id.curVersion);
        findViewById(R.id.checkVersion).setOnClickListener(this);
        curVersion.setText(getString(R.string.curVersion_s, ContextUtil.getPackageInfo().versionName));
    }

    @Override
    public void onClick(View v) {
        UpdateManager.start(new MyCall(this));
    }

    private class MyCall extends UpdateHandler {
        public MyCall(Context context) {
            super(context);
        }

        @Override
        public boolean isNewestVersion() {
            return false;
        }

        @Override
        public boolean forceUpdate() {
            return false;
        }

        @Override
        public String getDownloadUrl() {
            return "http://";
        }
    }
}
