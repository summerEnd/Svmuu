package com.svmuu.ui.activity.live;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gensee.taskret.OnTaskRet;
import com.gensee.view.GSVideoView;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.TextPainUtil;
import com.svmuu.R;
import com.svmuu.common.video.AbsVideoManager;
import com.svmuu.common.video.LiveManager;
import com.svmuu.common.video.VodManager;
import com.svmuu.ui.BaseFragment;


public class PlayFragment extends BaseFragment {

    public static final int TYPE_VIDEO_LIVE = 0;
    public static final int TYPE_AUDIO_LIVE = 1;
    public static final int TYPE_TEXT_LIVE = 2;
    VodManager vodManager;
    LiveManager liveManager;
    private TextView tv_subject;
    private TextView tv_live;
    private LinearLayout controlLayout;
    private GSVideoView gsView;
    private View videoLayout;
    String subject;

    private int mReason;
    private String vodId;
    private String vodPsw;
    private String mLiveId;
    private String mToken;
    private boolean isVod;
    private Callback callback;
    private boolean showMediaController = true;
    private int type;
    private boolean isClosed = false;
    private ImageView nolive;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.play_fragment, container, false);
    }

    /**
     * 用来判断当前是否已被关闭，在onActivityClose调用后为true
     *
     * @return true 已关闭，反之 false
     */
    public boolean isClosed() {
        return isClosed;
    }

    protected void initialize() {

        tv_subject = (TextView) findViewById(R.id.subject);
        tv_live = (TextView) findViewById(R.id.textLive);
        nolive = (ImageView) findViewById(R.id.no_live);
        controlLayout = (LinearLayout) findViewById(R.id.controlLayout);
        gsView = (GSVideoView) findViewById(R.id.gsView);
        videoLayout = findViewById(R.id.videoLayout);

        gsView.renderDrawble(getLoadingBitmap(ContextUtil.getString(R.string.connecting)), false);
        findViewById(R.id.fullScreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFullScreen();
            }
        });
        showMediaController(showMediaController);
    }

    Bitmap getLoadingBitmap(String text) {

        ColorDrawable drawable = new ColorDrawable(0xfff5f5f5);
        int width = 400;
        int height = 150;
        Bitmap src = Bitmap.createBitmap(
                width,
                height,
                drawable.getOpacity() != PixelFormat.OPAQUE
                        ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(src);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.BLACK);
        p.setTextSize(27);
        p.setTextAlign(Paint.Align.CENTER);
        float x = width / 2;
        float y = height / 2;

        canvas.drawText(text, x, y + TextPainUtil.getBaseLineOffset(p), p);
        return src;
    }

    /**
     * 播放录像
     *
     * @param vodId           录像id
     * @param pwd             录像密码
     * @param adjustVideoSize 是否调整视频宽高
     */
    public void playVod(String vodId, String pwd, boolean adjustVideoSize) {
        this.vodId = vodId;
        this.vodPsw = pwd;
        if (gsView == null) {
            isVod = true;
            return;
        }
       showCover(false);

        vodManager = VodManager.getInstance(getActivity());
        vodManager.setGSView(gsView);

        if (!vodManager.isPlaying()) {
            vodManager.adjustVideoSize(adjustVideoSize);
            vodManager.start(vodId, pwd);

        }
        showSwitchDialog(ContextUtil.getString(R.string.open_vod));
    }

    /**
     * 点播视频
     */
    public void playVod(String vodId, String pwd) {
        playVod(vodId, pwd, false);
    }

    public void showMediaController(boolean show) {
        this.showMediaController = show;
        if (controlLayout != null) {
            controlLayout.findViewById(R.id.fullScreen).setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }
    }

    /**
     * 直播
     */
    public void playLive(String liveId, String token) {
        this.mLiveId = liveId;
        this.mToken = token;

        if (gsView == null) {//如果当前是点播返回
            isVod = false;
            return;
        }

        if (isClosed()) {//如果已经关闭,返回
            return;
        }

        if (TextUtils.isEmpty(liveId)) {
            showCover(true);
            return;
        }
        liveManager = LiveManager.getInstance(getActivity());
        liveManager.addCallback(mCallback);
        if (liveManager.isPlaying()) {
            liveManager.setGSView(gsView);
            return;
        }

        boolean isReleased = liveManager.release(new OnTaskRet() {
            @Override
            public void onTaskRet(boolean b, int i, String s) {
                startLive();
            }
        });
        if (isReleased) {
            startLive();
        }
    }

    private AbsVideoManager.Callback mCallback=new AbsVideoManager.Callback() {
        @Override
        public void onVideoStarted() {
            showCover(false);
        }

        @Override
        public void onFailed() {
          showCover(true);
        }

        @Override
        public void onVideoReleased() {

        }
    };

    /**
     * 是否展示没有直播
     * @param show true 显示图片
     */
    void showCover(boolean show){
        nolive.setVisibility(show?View.VISIBLE:View.INVISIBLE);
    }

    /**
     * 启动直播
     */
    private void startLive() {
        liveManager.setGSView(gsView);
        liveManager.start(mLiveId, mToken);
        showSwitchDialog(ContextUtil.getString(R.string.open_living));
    }

    /**
     * 释放视频
     *
     * @return true 已经释放完毕，false 稍后释放
     */
    public boolean tryRelease() {
        if (isVod && vodManager != null) {
            vodManager.release();
        }

        if (!isVod && liveManager != null) {
            return liveManager.release(new OnTaskRet() {
                @Override
                public void onTaskRet(boolean b, int i, String s) {
                    onLeaveRoom("");
                }
            });
        }

        return true;
    }

    public boolean onActivityClose() {
        mReason = Reason.CLOSE_ACTIVITY;
        isClosed = true;
        if (tryRelease()) {
            if (getActivity() != null) {
                getActivity().finish();
            }
            return true;
        }
        showSwitchDialog(ContextUtil.getString(R.string.leaving));
        return false;
    }

    /**
     * 设置直播类型
     * 0、视频 1、音频 2、文字
     */
    public void setLiveType(int type) {
        switch (type) {
            case TYPE_VIDEO_LIVE: {
                setVideoVisible(true);

                if (this.type != 1) {
                    mReason = Reason.LIVE_VIDEO;
                    tryRelease();
                }
                break;
            }
            case TYPE_AUDIO_LIVE: {
                setVideoVisible(false);
                if (this.type != 0) {
                    mReason = Reason.LIVE_AUDIO;
                    tryRelease();
                }

                break;
            }
            case TYPE_TEXT_LIVE: {
                setVideoVisible(false);
                mReason = Reason.LIVE_TEXT;
                tryRelease();
                break;
            }
        }
        this.type = type;

    }

    public int getLiveType() {
        return type;
    }

    public void stop() {
        mReason = Reason.STOP_PLAY;

        if (!tryRelease()) {
            showSwitchDialog("正在关闭...");
        }
    }


    public void toFullScreen() {

        Intent intent = new Intent(getActivity(), FullScreenVideo.class);
        intent.putExtra(FullScreenVideo.EXTRA_IS_VOD, isVod);

        if (isVod) {
            intent.putExtra(FullScreenVideo.EXTRA_JOIN_TOKEN, vodPsw)
                    .putExtra(FullScreenVideo.EXTRA_LIVE_ID, vodId);
        } else {
            intent.putExtra(FullScreenVideo.EXTRA_JOIN_TOKEN, mToken)
                    .putExtra(FullScreenVideo.EXTRA_LIVE_ID, mLiveId);
        }
        startActivity(intent);
    }

    public void setVideoVisible(boolean visible) {
        if (visible) {
            videoLayout.setVisibility(View.VISIBLE);
        } else {
            videoLayout.setVisibility(View.GONE);
        }
    }


    /**
     * 尝试播放，是否有延迟加载的任务.
     * 如果已经关闭则不能播放
     */
    private void tryPlay() {

        if (isClosed) {
            return;
        }
        if (isVod && !TextUtils.isEmpty(vodId)) {
            playVod(vodId, vodPsw);
        } else if (!isVod && !TextUtils.isEmpty(mLiveId)) {
            if (type != TYPE_TEXT_LIVE) {
                playLive(mLiveId, mToken);
            }
        }
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


    public void onLeaveRoom(String msg) {

        if (callback != null) {
            callback.onReleased(mReason);
        }
        switch (mReason) {

            case Reason.LIVE_AUDIO: {
                if (mLiveId != null) {
                    gsView.renderDefault();
                    liveManager.setGSView(null);
                    liveManager.start(mToken, mLiveId);
                }
                break;
            }
            case Reason.LIVE_VIDEO: {
                if (mLiveId != null) {
                    liveManager.setGSView(gsView);
                    liveManager.start(mToken, mLiveId);
                }
                break;
            }
            case Reason.LIVE_TEXT: {
                if (mLiveId != null) {
                    liveManager.setGSView(null);
                }
                break;
            }
            case Reason.CLOSE_ACTIVITY: {
                if (getActivity() != null) {
                    getActivity().finish();
                }
                break;
            }
            case Reason.TO_VOD: {
                isVod = true;
                vodManager.start(vodId, vodPsw);
                break;
            }

            case Reason.TO_LIVE: {
                isVod = false;
                playLive(mLiveId, mToken);
                break;
            }
            case Reason.STOP_PLAY: {
                break;
            }
        }
    }

    public interface Reason {
        int LIVE_VIDEO = 0;
        int LIVE_AUDIO = 1;
        int LIVE_TEXT = 2;
        int FULL_SCREEN = 3;

        int CLOSE_ACTIVITY = 4;
        int TO_VOD = 5;
        int TO_LIVE = 6;

        int STOP_PLAY = 10;
    }

    @Override
    public void onResume() {
        super.onResume();
        tryPlay();
    }

    private void showSwitchDialog(String message) {

        if (gsView != null) {
//            gsView.renderDrawble(getLoadingBitmap(message), false);
        }
    }

    public interface Callback {
        void onReleased(int reason);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mReason = Reason.STOP_PLAY;
        tryRelease();
        if (liveManager!=null){
            liveManager.removeCallback(mCallback);
        }
        if (vodManager!=null){
            vodManager.removeCallback(mCallback);
        }
    }

}
