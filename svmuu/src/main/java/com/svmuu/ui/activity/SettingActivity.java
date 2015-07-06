package com.svmuu.ui.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.VersionUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sp.lib.common.preference.PreferenceUtil;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.FileUtil;
import com.svmuu.AppDelegate;
import com.svmuu.R;
import com.svmuu.common.config.Preference;
import com.svmuu.common.http.HttpHandler;
import com.svmuu.common.http.HttpManager;
import com.svmuu.common.http.Response;
import com.svmuu.ui.BaseActivity;
import com.svmuu.ui.pop.YAlertDialog;

import org.apache.http.Header;
import org.json.JSONException;

import java.io.File;

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
        switch (v.getId()) {
            case R.id.aboutUs: {
                break;
            }
            case R.id.suggestion: {
                break;
            }
            case R.id.clear: {
                File cacheDir = getCacheDir();
                FileUtil.delete(cacheDir);
                YAlertDialog.show(this, getString(R.string.tips), getString(R.string.cache_clear_ok));
                break;
            }
            case R.id.logout: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.warn);
                builder.setMessage(R.string.confirm_logout);
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SRequest request = new SRequest("logout");
                        HttpManager.getInstance().postMobileApi(SettingActivity.this, request, new HttpHandler() {
                            @Override
                            public void onResultOk(int statusCode, Header[] headers, Response response) throws JSONException {
                                AppDelegate.getInstance().setUser(null);
                                SharedPreferences sp = Preference.get(getApplicationContext(), Preference.USER.class);
                                sp.edit()
                                        .putString(Preference.USER.USER_PASSWORD, "")
                                        .putBoolean(Preference.USER.IS_SAVE_PASSWORD, false)
                                        .apply();
                                finish();
                            }
                        });
                    }
                });
                builder.show();
                break;
            }
        }
        super.onClick(v);
    }
}
