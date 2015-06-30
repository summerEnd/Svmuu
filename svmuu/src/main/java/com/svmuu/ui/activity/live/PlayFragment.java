package com.svmuu.ui.activity.live;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gensee.view.GSVideoView;
import com.svmuu.R;
import com.svmuu.common.LiveManager;
import com.svmuu.common.VodManager;
import com.svmuu.ui.BaseFragment;
import com.svmuu.ui.pop.ProgressIDialog;


public class PlayFragment extends BaseFragment implements LiveManager.Callback {

    VodManager vodManager;
    LiveManager liveManager;
    private TextView tv_subject;
    private TextView tv_live;
    private ImageView nolive;
    private LinearLayout controlLayout;
    private GSVideoView gsView;
    private View videoLayout;
    String subject;
    ProgressIDialog progressIDialog;
    private int mReason;
    private String vodId;
    private String vodPsw;
    private String live_id;
    private String token;
    private boolean isVod;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.play_fragment, container, false);
    }

    /**
     * 点播视频
     */
    public void playVod(String vodId, String pwd) {
        this.vodId = vodId;
        this.vodPsw = pwd;
        if (vodManager == null) {
            vodManager = VodManager.getInstance(getActivity(), gsView);
        }

    }

    /**
     * 直播
     */
    public void playLive(String liveId, String token) {
        this.live_id = liveId;
        this.token = token;
        if (liveManager == null) {
            liveManager = LiveManager.getInstance();
        }
        liveManager.setUp(getActivity(), gsView, this);
    }

    /**
     * 释放视频
     *
     * @return true 已经释放完毕，false 稍后释放
     */
    public boolean tryRelease() {
        if (vodManager != null) {
            vodManager.release();
        }

        return liveManager == null || liveManager.tryRelease();

    }

    /**
     * 设置直播类型
     * 1、视频 2、音频 3、文字
     */
    public void setLiveType(int type) {
        boolean isReleased = tryRelease();
        switch (type) {
            case 1: {
                mReason = Reason.LIVE_VIDEO;
                break;
            }
            case 2: {
                mReason = Reason.LIVE_AUDIO;
                break;
            }
            case 3: {
                mReason = Reason.LIVE_TEXT;
                break;
            }
        }
        if (isReleased) {
            onLeaveRoom("");
        }
    }

    public boolean stop() {
        return false;
    }


    public void toFullScreen() {

        if (tryRelease()) {
            if (isVod) {
                startActivity(new Intent(getActivity(), FullScreenVideo.class)
                                .putExtra(FullScreenVideo.EXTRA_IS_VOD, true)
                                .putExtra(FullScreenVideo.EXTRA_JOIN_TOKEN, vodPsw)
                                .putExtra(FullScreenVideo.EXTRA_LIVE_ID, vodId)
                );
            } else {
                startActivity(new Intent(getActivity(), FullScreenVideo.class)
                                .putExtra(FullScreenVideo.EXTRA_IS_VOD, false)
                                .putExtra(FullScreenVideo.EXTRA_JOIN_TOKEN, token)
                                .putExtra(FullScreenVideo.EXTRA_LIVE_ID, live_id)
                );
            }
        } else {
            mReason = Reason.FULL_SCREEN;
        }
    }

    public void setVideoVisible(boolean visible) {
        if (visible) {
            videoLayout.setVisibility(View.VISIBLE);
        } else {
            videoLayout.setVisibility(View.GONE);
        }
    }


    protected void initialize() {

        tv_subject = (TextView) findViewById(R.id.subject);
        tv_live = (TextView) findViewById(R.id.textLive);
        nolive = (ImageView) findViewById(R.id.no_live);
        controlLayout = (LinearLayout) findViewById(R.id.controlLayout);
        gsView = (GSVideoView) findViewById(R.id.gsView);
        videoLayout = findViewById(R.id.videoLayout);
        findViewById(R.id.fullScreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFullScreen();
            }
        });
    }

    public void setSubject(String subject) {
        this.subject = subject;
        requestRefreshUI();
    }

    @Override
    public void refreshUI() {
        tv_subject.setText(subject);
        tv_live.setText(subject);
    }

    @Override
    public void onResult(boolean success) {

    }

    @Override
    public void onLeaveRoom(String msg) {
        //这里可以弹出对话框，确定后在关闭界面
        if (progressIDialog != null) {
            progressIDialog.dismiss();
        }
        switch (mReason) {
            case Reason.FULL_SCREEN: {
                toFullScreen();
                break;
            }
            case Reason.LIVE_AUDIO: {
                if (live_id != null) {
                    gsView.renderDefault();
                    liveManager.setGSView(null);
                    liveManager.startPlay(token, "LINCOLN", live_id);
                }
                break;
            }
            case Reason.LIVE_VIDEO: {
                if (live_id != null) {
                    liveManager.setGSView(gsView);
                    liveManager.startPlay(token, "LINCOLN", live_id);
                }
                break;
            }
            case Reason.LIVE_TEXT: {
                if (live_id != null) {
                    liveManager.setGSView(null);
                }
                break;
            }
            case Reason.CLOSE: {
                getActivity().finish();
                break;
            }
            case Reason.TO_VIDEO: {
                isVod = true;
                vodManager.start(vodId, vodPsw);
                break;
            }
        }
    }

    public interface Reason {
        int LIVE_VIDEO = 0;
        int LIVE_AUDIO = 1;
        int LIVE_TEXT = 2;
        int FULL_SCREEN = 3;

        int CLOSE = 4;
        int TO_VIDEO = 5;
    }
}
