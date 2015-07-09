package com.svmuu.common.video;

import android.app.Activity;

import com.gensee.common.ServiceType;
import com.gensee.entity.InitParam;
import com.gensee.net.RtComp;
import com.gensee.room.RtSdk;
import com.gensee.taskret.OnTaskRet;
import com.gensee.view.GSVideoView;

import java.util.ArrayList;

public class LiveManager extends AbsVideoManager {

    private static LiveManager manager;
    RtImpl mRtImpl;
    private boolean isPlaying = false;
    private ArrayList<Callback> callbacks;

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
                getContext().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dispatchVideoStart();
                        isPlaying = true;
                    }
                });
            }
        };
    }

    @Override
    public void onStart(String id, String token) {
        InitParam p = new InitParam();
        String domain = "svmuu.gensee.com";
        String account = "admin@svmuu.com";
        String pwd = "888888";

        //domain
        p.setDomain(domain);
        //编号（直播间号）,如果没有编号却有直播id的情况请使用setLiveId("此处直播id或课堂id");
        //				p.setNumber(number);
        p.setLiveId(id);
        //				p.setLiveId("b9743c6063934ec4ac2fb6bc83951d80");
        //站点认证帐号，根据情况可以填""
        p.setLoginAccount(account);
        //站点认证密码，根据情况可以填""
        p.setLoginPwd(pwd);
        //昵称，供显示用
        p.setNickName(getNickName());
        //加入口令，没有则填""
        p.setJoinPwd(token);
        //站点类型ServiceType.ST_CASTLINE 直播webcast，
        //ServiceType.ST_MEETING 会议meeting，
        //ServiceType.ST_TRAINING 培训
        p.setServiceType(ServiceType.ST_CASTLINE);

        RtComp comp = new RtComp(getContext().getApplicationContext(),
                new RtComp.Callback() {
                    @Override
                    public void onInited(String s) {
                        mRtImpl.joinWithParam("", s);
                    }

                    @Override
                    public void onErr(final int i) {
                        isPlaying = false;

                        getContext().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dispatchFailed();
                            }
                        });
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


    public boolean release(OnTaskRet taskRet) {
        isPlaying = false;
        return mRtImpl.getRtSdk().leave(false, taskRet);
    }

}
