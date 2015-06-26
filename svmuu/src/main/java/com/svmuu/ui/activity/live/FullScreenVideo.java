package com.svmuu.ui.activity.live;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.gensee.view.GSVideoView;
import com.svmuu.R;
import com.svmuu.common.LiveManager;
import com.svmuu.ui.pop.YAlertDialog;

public class FullScreenVideo extends Activity {

    private LiveManager manager;
    public static final String EXTRA_JOIN_TOKEN="token";
    public static final String EXTRA_NICK_NAME="nick";
    public static final String EXTRA_LIVE_ID="id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_full_sceen_video);
        GSVideoView viewById = (GSVideoView) findViewById(R.id.gsView);

        manager = LiveManager.getInstance();
        manager.setCallback(new LiveManager.Callback() {
            @Override
            public void onResult(boolean success) {
                findViewById(R.id.no_live).setVisibility(success ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onLeaveRoom(String msg) {

                finish();
            }
        });
        manager.setGSView(viewById);
        Intent i=getIntent();
        manager.startPlay(
                i.getStringExtra(EXTRA_JOIN_TOKEN),
                i.getStringExtra(EXTRA_NICK_NAME),
                i.getStringExtra(EXTRA_LIVE_ID)
                );
    }

    @Override
    public void onBackPressed() {
       manager.tryRelease();
    }



}