package com.svmuu.common.video;

import android.app.Activity;
import android.text.TextUtils;

import com.gensee.view.GSVideoView;
import com.svmuu.AppDelegate;

public abstract class AbsVideoManager {
    private Activity context;
    private Callback callback;

    /**
     * 启动视频
     *
     * @param id    视频id
     * @param token 加入口令
     */
    public final void start(String id, String token) {
        onStart(id, token);
        if (callback != null) {
            callback.onVideoStarted();
        }
    }

    /**
     * @param gsView 用来展示视频
     */
    public abstract void setGSView(GSVideoView gsView);

    /**
     * 释放当前视频
     *
     * @return true 成功
     */
    public final boolean release() {
        if (callback != null) {
            callback.onVideoReleased();
        }
        return onRelease();
    }

    /**
     * 子类释放视频的操作在这里实现
     *
     * @return true 陈宫
     */
    protected abstract boolean onRelease();

    /**
     * 子类启动视频的操作在这里实现
     */
    protected abstract void onStart(String id, String token);

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    /**
     * @return 当前登录用户的昵称
     */
    public String getNickName() {
        String nickName = "";
        try {
            nickName = AppDelegate.getInstance().getUser().name;
        } catch (Exception ignored) {
        } finally {
            if (TextUtils.isEmpty(nickName)) {
                nickName = "";
            }
        }
        return nickName;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public abstract boolean isPlaying();

    public interface Callback {
        void onVideoStarted();

        void onVideoReleased();
    }
}

