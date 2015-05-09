package com.yjy998.ui.activity.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;
import com.yjy998.common.util.VersionUtil;
import com.yjy998.ui.activity.base.SecondActivity;

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
        VersionUtil.start(this);
    }


}
