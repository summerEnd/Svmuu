package com.svmuu.ui.activity.live;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.gensee.view.GSVideoView;
import com.svmuu.R;
import com.svmuu.common.LiveManager;
import com.svmuu.common.VodManager;
import com.svmuu.ui.pop.ProgressIDialog;

public class FullScreenVideo extends Activity {

    private LiveManager liveManager;
    private VodManager vodManager;

    //加入口令
    public static final String EXTRA_JOIN_TOKEN = "token";
    //加入昵称
    public static final String EXTRA_NICK_NAME = "nick";
    //视频id
    public static final String EXTRA_LIVE_ID = "id";
    //是否为录像
    public static final String EXTRA_IS_VOD = "is_vod";
    private GSVideoView gsView;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_full_sceen_video);
        gsView = (GSVideoView) findViewById(R.id.gsView);

        if (getIntent().getBooleanExtra(EXTRA_IS_VOD, false)) {
            setUpVod();
        } else {
            setUpLive();
        }
    }

    @Override
    public void onBackPressed() {
        if (vodManager != null) {
            vodManager.release();
            super.onBackPressed();
        } else if (liveManager != null) {
            liveManager.tryRelease();
            ProgressIDialog.show(this, "正在退出房间...");
        } else {
            super.onBackPressed();
        }

    }

    void setUpLive() {
        liveManager = LiveManager.getInstance();
        liveManager.setUp(this, gsView, new LiveManager.Callback() {
            @Override
            public void onResult(boolean success) {

            }

            @Override
            public void onLeaveRoom(String msg) {
                finish();
            }
        });

        Intent i = getIntent();
        liveManager.startPlay(
                i.getStringExtra(EXTRA_JOIN_TOKEN),
                i.getStringExtra(EXTRA_NICK_NAME),
                i.getStringExtra(EXTRA_LIVE_ID)
        );
    }

    void setUpVod() {
        vodManager = VodManager.newInstance(this, gsView);

        Intent i = getIntent();
        id = i.getStringExtra(EXTRA_LIVE_ID);
        String password = i.getStringExtra(EXTRA_JOIN_TOKEN);
        vodManager.start(
                id,
                password
        );
    }

}