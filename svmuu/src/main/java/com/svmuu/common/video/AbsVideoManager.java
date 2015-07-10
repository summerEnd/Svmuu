package com.svmuu.common.video;

import android.app.Activity;
import android.text.TextUtils;

import com.gensee.view.GSVideoView;
import com.svmuu.AppDelegate;

import java.util.ArrayList;

public abstract class AbsVideoManager {
    private Activity context;
    private ArrayList<Callback> callbacks = new ArrayList<>();
    public static final String domain = "svmuu.gensee.com";
    public static final String account = "admin@svmuu.com";
    public static final String pwd = "888888";
    protected String videoId;
    protected String token;

    /**
     * 启动视频
     *
     * @param id    视频id
     * @param token 加入口令
     */
    public final void start(String id, String token) {
        this.token=token;
        this.videoId=id;
        onStart(id, token);

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

        return onRelease();
    }

    /**
     * 子类释放视频的操作在这里实现
     *
     * @return true 陈宫
     */
    protected abstract boolean onRelease();

    /**
     * 暂停当前视频
     *
     * @return true 暂停成功
     */
    public abstract boolean pause();

    /**
     * 恢复当前视频
     *
     * @return true 成功
     */
    public abstract boolean resume();

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

    public boolean removeCallback(Callback callback) {
        return callbacks.remove(callback);
    }

    public void addCallback(Callback callback) {
        if (!callbacks.contains(callback))
            callbacks.add(callback);
    }

    public final void dispatchVideoStart() {
        getContext().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Callback callback : callbacks) {
                    callback.onVideoStarted();
                }
            }
        });

    }

    public final void dispatchVideoReleased() {
        getContext().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Callback callback : callbacks) {
                    callback.onVideoReleased();
                }
            }
        });

    }

    public final void dispatchFailed() {
        getContext().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Callback callback : callbacks) {
                    callback.onFailed();
                }
            }
        });

    }

    public abstract boolean isPlaying();

    public abstract void destroy();

    public interface Callback {
        void onVideoStarted();

        void onFailed();

        void onVideoReleased();
    }
}

