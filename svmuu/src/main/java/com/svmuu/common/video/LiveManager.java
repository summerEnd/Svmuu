package com.svmuu.common.video;

import android.app.Activity;
import android.text.TextUtils;

import com.gensee.common.ServiceType;
import com.gensee.entity.InitParam;
import com.gensee.net.RtComp;
import com.gensee.room.RtSdk;
import com.gensee.routine.UserInfo;
import com.gensee.taskret.OnTaskRet;
import com.gensee.view.GSVideoView;
import com.sp.lib.common.util.ContextUtil;
import com.svmuu.common.http.HttpManager;

import java.util.ArrayList;

public class LiveManager extends AbsVideoManager {

    private static LiveManager manager;
    RtImpl mRtImpl;
    private boolean isPlaying = false;

    public static LiveManager getInstance(Activity activity) {
        if (manager == null) {
            manager = new LiveManager(activity);
        }
        return manager;
    }

    private LiveManager(Activity activity) {
        setContext(activity);
        mRtImpl = new RtImpl(activity) {
            @Override
            protected void onVideoStart() {
                super.onVideoStart();
                isPlaying = true;
                dispatchVideoStart();
            }
        };
    }

    @Override
    public void onStart(String id, String token) {

        if (TextUtils.isEmpty(id)) {
            return;
        }
        InitParam p = new InitParam();


        //domain
        p.setDomain(domain);
        //编号（直播间号）,如果没有编号却有直播id的情况请使用setLiveId("此处直播id或课堂id");
        p.setLiveId(id);
        //站点认证帐号，根据情况可以填""
        p.setLoginAccount(account);
        //站点认证密码，根据情况可以填""
        p.setLoginPwd(pwd);
        //昵称，供显示用
        p.setNickName(getNickName());
        //加入口令，没有则填""
        p.setJoinPwd(token);
        p.setServiceType(ServiceType.ST_CASTLINE);

        RtComp comp = new RtComp(getContext().getApplicationContext(),
                new RtComp.Callback() {
                    @Override
                    public void onInited(String s) {
                        mRtImpl.joinWithParam("", s);
                    }

                    @Override
                    public void onErr(int i) {
                        isPlaying = false;
                        if (i != 1 && i != 3) {
                            dispatchFailed();
                        }
                        ContextUtil.toast_debug(i);

                    }
                });
        comp.initWithGensee(p);
    }


    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void destroy() {
        release();
        manager = null;
    }

    @Override
    public void setGSView(GSVideoView gsView) {
        mRtImpl.setGsVideoView(gsView);
    }

    @Override
    public boolean onRelease() {
        RtSdk rtSdk = mRtImpl.getRtSdk();
        boolean leave = rtSdk.leave(false, new OnTaskRet() {
            @Override
            public void onTaskRet(boolean b, int i, String s) {
                dispatchVideoReleased();
            }
        });
        if (leave) {
            dispatchVideoReleased();
        }
        isPlaying = false;
        return leave;
    }

    @Override
    public boolean pause() {
        return release();
    }

    @Override
    public boolean resume() {
        start(videoId, token);
        return false;
    }


    public boolean release(OnTaskRet taskRet) {
        isPlaying = false;
        return mRtImpl.getRtSdk().leave(false, taskRet);
    }


}
