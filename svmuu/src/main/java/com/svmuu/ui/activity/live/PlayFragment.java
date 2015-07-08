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

import com.gensee.entity.DocInfo;
import com.gensee.taskret.OnTaskRet;
import com.gensee.view.GSVideoView;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.SLog;
import com.sp.lib.common.util.TextPainUtil;
import com.svmuu.AppDelegate;
import com.svmuu.R;
import com.svmuu.common.LiveManager;
import com.svmuu.common.VodManager;
import com.svmuu.ui.BaseFragment;
import com.svmuu.ui.pop.ProgressIDialog;

import java.util.List;


public class PlayFragment extends BaseFragment implements LiveManager.Callback {

    public static final int TYPE_VIDEO_LIVE = 0;
    public static final int TYPE_AUDIO_LIVE = 1;
    public static final int TYPE_TEXT_LIVE = 2;
    VodManager vodManager;
    LiveManager liveManager;
    private TextView tv_subject;
    private TextView tv_live;
    private ImageView nolive;
    private LinearLayout controlLayout;
    private GSVideoView gsView;
    private View videoLayout;
    String subject;
    //暂时不用弹出进度框
    ProgressIDialog progressIDialog;
    private int mReason;
    private String vodId;
    private String vodPsw;
    private String mLiveId;
    private String mToken;
    private boolean isVod;
    private Callback callback;
    private boolean showMediaController = true;
    private int type;
    private String nickName;
    private boolean isClosed = false;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        nickName = AppDelegate.getInstance().getUser().name;

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

        gsView.renderDrawble(getBitmap(), false);
        findViewById(R.id.fullScreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFullScreen();
            }
        });
        showMediaController(showMediaController);
    }

    Bitmap getBitmap() {

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
        String prom = "加载中...";
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.BLACK);
        p.setTextSize(27);
        p.setTextAlign(Paint.Align.CENTER);
        float x = width / 2;
        float y = height / 2;

        canvas.drawText(prom, x, y + TextPainUtil.getBaseLineOffset(p), p);
        return src;
    }

    public void playVod(String vodId, String pwd, boolean ajustVideoSize) {
        this.vodId = vodId;
        this.vodPsw = pwd;
        if (gsView == null) {
            isVod = true;
            return;
        }

        if (vodManager == null) {
            vodManager = VodManager.newInstance(getActivity(), gsView);
            vodManager.setCallback(vodManager.new SimpleVodListener());
        }

        vodManager.release();
        vodManager.adjustVideoSize(ajustVideoSize);
        nolive.setVisibility(View.INVISIBLE);
        vodManager.start(vodId, pwd);
        showSwitchDialog(ContextUtil.getString(R.string.open_vod));
    }

    /**
     * 点播视频
     */
    public void playVod(String vodId, String pwd) {
        playVod(vodId, pwd, false);
    }

    private void tryDismiss() {
        try {
//            if (progressBar != null) {
//                progressBar.setVisibility(View.INVISIBLE);
//            }
            progressIDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        SLog.debug("liveId:" + liveId + "  mToken:" + token);
        if (gsView == null) {
            //如果当前是点播返回
            isVod = false;
            return;
        }

        if (isClosed()) {
            //如果已经关闭,返回
            return;
        }

        if (liveManager == null) {
            liveManager = LiveManager.getInstance();
        }
        boolean isReleased = liveManager.tryRelease(new OnTaskRet() {
            @Override
            public void onTaskRet(boolean b, int i, String s) {
                startLive();
            }
        });
        if (isReleased) {
            startLive();
        }
    }

    /**
     * 启动直播
     */
    private void startLive() {
        liveManager.setUp(getActivity(), gsView, PlayFragment.this);
        liveManager.startPlay(mToken, nickName, mLiveId);
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
            return true;
        }

        if (!isVod && liveManager != null) {
            return liveManager.tryRelease(new OnTaskRet() {
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

        if (tryRelease()) {
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


    @Override
    public void onLiveJoint() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tryDismiss();
                    nolive.setVisibility(View.INVISIBLE);
                }
            });
        }

    }

    @Override
    public void onLeaveRoom(String msg) {
        //这里可以弹出对话框，确定后在关闭界面
        tryDismiss();

        if (callback != null) {
            callback.onReleased(mReason);
        }
        switch (mReason) {
            case Reason.FULL_SCREEN: {
                toFullScreen();
                break;
            }
            case Reason.LIVE_AUDIO: {
                if (mLiveId != null) {
                    gsView.renderDefault();
                    liveManager.setGSView(null);
                    liveManager.startPlay(mToken, nickName, mLiveId);
                }
                break;
            }
            case Reason.LIVE_VIDEO: {
                if (mLiveId != null) {
                    liveManager.setGSView(gsView);
                    liveManager.startPlay(mToken, nickName, mLiveId);
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

//        if (progressBar != null) {
////            progressBar.setVisibility(View.VISIBLE);
//        }
        if (getActivity() == null) {
            return;
        }
//        if (progressIDialog == null) {
//            progressIDialog = new ProgressIDialog(getActivity());
//        } else {
//            tryDismiss();
//        }
//        progressIDialog.setMessage(message);
//        progressIDialog.show();
    }

    public interface Callback {
        void onReleased(int reason);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mReason = Reason.STOP_PLAY;
        tryRelease();
    }
}
