package com.svmuu.ui.activity.live;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.gensee.taskret.OnTaskRet;
import com.gensee.view.GSVideoView;
import com.sp.lib.common.util.ContextUtil;
import com.svmuu.R;
import com.svmuu.common.video.AbsVideoManager;
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

    private boolean isPaused = false;

    GSVideoView gsVideoView;

    AbsVideoManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_full_sceen_video);
        gsVideoView = (GSVideoView) findViewById(R.id.gsView);

        if (getIntent().getBooleanExtra(EXTRA_IS_VOD, false)) {
            manager = VodManager.getInstance(this);
        } else {
            manager = LiveManager.getInstance(this);
        }
        manager.setGSView(gsVideoView);

        findViewById(R.id.exit_full_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        registerReceiver(mHomeKeyEventReceiver, new IntentFilter(
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mHomeKeyEventReceiver);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isPaused) {
            manager.resume();
        }
    }

    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    //表示按了home键,程序到了后台
//                   if (manager instanceof VodManager){
//                       manager.pause();
//                       isPaused = true;
//                   }

                }
            }
        }
    };
}