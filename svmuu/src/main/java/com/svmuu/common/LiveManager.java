package com.svmuu.common;


import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.gensee.common.ServiceType;
import com.gensee.entity.InitParam;
import com.gensee.net.RtComp;
import com.gensee.room.RtSdk;
import com.gensee.room.RtSimpleImpl;
import com.gensee.routine.IRTEvent;
import com.gensee.routine.State;
import com.gensee.routine.UserInfo;
import com.gensee.view.GSVideoView;
import com.sp.lib.common.util.ContextUtil;
import com.svmuu.R;

public class LiveManager implements RtComp.Callback {

    private RtSimpleImpl simpleImpl = new SimImpl();
    private Activity context;
    UserInfo self;
    @Nullable
    GSVideoView gsVideoView;
    Callback callback;
    private static LiveManager INSTANCE;

    public interface Callback {
        void onResult(boolean success);

        void onLeaveRoom(String msg);
    }

    public static LiveManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LiveManager();
        }
        return INSTANCE;
    }

    private LiveManager() {

    }

    public void setUp(Activity context, GSVideoView gsVideoView, Callback callback) {
        this.context = context;
        this.gsVideoView = gsVideoView;
        this.callback = callback;
        simpleImpl.setVideoView(gsVideoView);
    }

    public void setGSView(GSVideoView view) {
        gsVideoView = view;
        simpleImpl.setVideoView(view);
    }


    public void startPlay(String joinPwd, String nickName, String liveId) {
        InitParam p = new InitParam();
        String domain = "svmuu.gensee.com";
        String account = "admin@svmuu.com";
        String pwd = "888888";

        //domain
        p.setDomain(domain);
        //编号（直播间号）,如果没有编号却有直播id的情况请使用setLiveId("此处直播id或课堂id");
        //				p.setNumber(number);
        p.setLiveId(liveId);
        //				p.setLiveId("b9743c6063934ec4ac2fb6bc83951d80");
        //站点认证帐号，根据情况可以填""
        p.setLoginAccount(account);
        //站点认证密码，根据情况可以填""
        p.setLoginPwd(pwd);
        //昵称，供显示用
        p.setNickName(nickName);
        //加入口令，没有则填""
        p.setJoinPwd(joinPwd);
        //站点类型ServiceType.ST_CASTLINE 直播webcast，
        //ServiceType.ST_MEETING 会议meeting，
        //ServiceType.ST_TRAINING 培训
        p.setServiceType(ServiceType.ST_CASTLINE);

        RtComp comp = new RtComp(context.getApplicationContext(),
                this);
        comp.initWithGensee(p);
    }

    /**
     * 退出的时候请调用
     */
    public boolean leaveCast() {
        //TODO 显示进度框
        simpleImpl.leave(false);
        return self == null;
    }

    public boolean tryRelease() {

        if (self==null){

            return true;
        }

        return leaveCast();
    }

    @Override
    public void onInited(String s) {
        simpleImpl.joinWithParam("", s);
    }

    @Override
    public void onErr(int i) {
        dispatchResult(false);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    void dispatchResult(boolean success) {
        if (callback != null) {
            callback.onResult(success);
        }
    }

    private class SimImpl extends RtSimpleImpl {
        @Override
        protected void onVideoStart() {

        }

        @Override
        protected void onVideoEnd() {

        }

        @Override
        public void onJoin(boolean b) {

        }

        @Override
        public void onRoomJoin(final int result, UserInfo self) {
            super.onRoomJoin(result, self);
            LiveManager.this.self = self;
            context.runOnUiThread(new Runnable() {
                public void run() {
                    dispatchResult(true);
                    String resultDesc;
                    switch (result) {
                        //加入成功  除了成功其他均需要正常提示给用户
                        case IRTEvent.IRoomEvent.JoinResult.JR_OK:
                            resultDesc = "您已加入成功";

                            break;
                        //加入错误
                        case IRTEvent.IRoomEvent.JoinResult.JR_ERROR:
                            resultDesc = null;
                            break;
                        //课堂被锁定
                        case IRTEvent.IRoomEvent.JoinResult.JR_ERROR_LOCKED:
                            resultDesc = "直播间已被锁定";

                            break;
                        //老师（组织者已经加入）
                        case IRTEvent.IRoomEvent.JoinResult.JR_ERROR_HOST:
                            resultDesc = "老师已经加入，请以其他身份加入";
                            break;
                        //加入人数已满
                        case IRTEvent.IRoomEvent.JoinResult.JR_ERROR_LICENSE:
                            resultDesc = "人数已满，联系管理员";

                            break;
                        //音视频编码不匹配
                        case IRTEvent.IRoomEvent.JoinResult.JR_ERROR_CODEC:
                            resultDesc = "编码不匹配";
                            break;
                        //超时
                        case IRTEvent.IRoomEvent.JoinResult.JR_ERROR_TIMESUP:
                            resultDesc = "已经超过直播结束时间";
                            break;

                        default:
                            resultDesc = "其他结果码：" + result + "联系管理员";
                            break;
                    }
                    if (!TextUtils.isEmpty(resultDesc)) {
                        ContextUtil.toast(resultDesc);
                    }
                }
            });
        }


        /**
         * 直播状态 s.getValue()   0 默认直播未开始 1、直播中， 2、直播停止，3、直播暂停
         */
        @Override
        public void onRoomPublish(State s) {
            super.onRoomPublish(s);
            //TODO 此逻辑是控制视频要在直播开始后才准许看的逻辑
            byte castState = s.getValue();
            RtSdk rtSdk = getRtSdk();

            switch (castState) {
                case 1:
                    setVideoView(gsVideoView);
                    rtSdk.audioOpenSpeaker(null);
                    break;
                case 0:
                case 2:
                case 3:
                    if (gsVideoView != null) {
                        gsVideoView.renderDrawble(BitmapFactory.decodeResource(context.getResources(), R.drawable.no_live), false);
                    }
                    setVideoView(null);
                    rtSdk.audioCloseSpeaker(null);
                default:
                    break;
            }
        }

        //退出完成 关闭界面
        @Override
        protected void onRelease(final int reason) {
            //reason 退出原因
            context.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    String msg = "已退出";
                    switch (reason) {
                        //用户自行退出  正常退出
                        case IRTEvent.IRoomEvent.LeaveReason.LR_NORMAL:
                            msg = "您已经成功退出";
                            break;
                        //LR_EJECTED = LR_NORMAL + 1; //被踢出
                        case IRTEvent.IRoomEvent.LeaveReason.LR_EJECTED:
                            msg = "您已被踢出";
                            break;
                        //LR_TIMESUP = LR_NORMAL + 2; //时间到
                        case IRTEvent.IRoomEvent.LeaveReason.LR_TIMESUP:
                            msg = "时间已过";
                            break;
                        //LR_CLOSED = LR_NORMAL + 3; //直播（课堂）已经结束（被组织者结束）
                        case IRTEvent.IRoomEvent.LeaveReason.LR_CLOSED:
                            msg = "直播间已经被关闭";
                            break;

                        default:
                            break;
                    }

                    if (callback != null) {
                        callback.onLeaveRoom(msg);
                    }


                }
            });
        }

        @Override
        public Context onGetContext() {
            return context.getBaseContext();
        }
    }
}
