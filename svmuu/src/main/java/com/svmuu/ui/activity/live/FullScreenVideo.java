package com.svmuu.ui.activity.live;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.gensee.taskret.OnTaskRet;
import com.gensee.view.GSVideoView;
import com.svmuu.R;
import com.svmuu.common.video.VodManager;
import com.svmuu.common.video.LiveManager;

public class FullScreenVideo extends FragmentActivity {


    //加入口令
    public static final String EXTRA_JOIN_TOKEN = "token";
    //加入昵称
    public static final String EXTRA_NICK_NAME = "nick";
    //视频id
    public static final String EXTRA_LIVE_ID = "id";
    //是否为录像
    public static final String EXTRA_IS_VOD = "is_vod";
    //直播主题
    public static final String EXTRA_SUBJECT = "subject";


    GSVideoView gsVideoView;

    LiveManager liveManager;
    VodManager vodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_full_sceen_video);
        gsVideoView = (GSVideoView) findViewById(R.id.gsView);

        if (getIntent().getBooleanExtra(EXTRA_IS_VOD, false)) {
            vodManager = VodManager.getInstance(this);
            vodManager.setGSView(gsVideoView);
        } else {
            liveManager = LiveManager.getInstance(this);
            liveManager.setGSView(gsVideoView);
        }
    }
}