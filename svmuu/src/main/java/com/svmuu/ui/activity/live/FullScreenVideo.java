package com.svmuu.ui.activity.live;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.svmuu.R;
import com.svmuu.common.LiveManager;
import com.svmuu.ui.pop.ProgressIDialog;

public class FullScreenVideo extends Activity {


    //加入口令
    public static final String EXTRA_JOIN_TOKEN = "token";
    //加入昵称
    public static final String EXTRA_NICK_NAME = "nick";
    //视频id
    public static final String EXTRA_LIVE_ID = "id";
    //是否为录像
    public static final String EXTRA_IS_VOD = "is_vod";
    private String id;

    PlayFragment mPlayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_full_sceen_video);
        mPlayFragment = new PlayFragment();
        mPlayFragment.showMediaController(false);
        if (getIntent().getBooleanExtra(EXTRA_IS_VOD, false)) {

            Intent i = getIntent();
            id = i.getStringExtra(EXTRA_LIVE_ID);
            String password = i.getStringExtra(EXTRA_JOIN_TOKEN);
            mPlayFragment.playVod(
                    id,
                    password
            );
        } else {

            Intent i = getIntent();
            mPlayFragment.playLive(
                    i.getStringExtra(EXTRA_JOIN_TOKEN),
                    i.getStringExtra(EXTRA_LIVE_ID)
            );
        }
    }

    @Override
    public void onBackPressed() {
        mPlayFragment.onActivityClose();
    }

}