package com.svmuu.common;


import android.app.Activity;
import android.content.Context;

import com.gensee.common.ServiceType;
import com.gensee.entity.InitParam;
import com.gensee.net.RtComp;
import com.gensee.room.RtSimpleImpl;
import com.gensee.routine.IRTEvent;
import com.gensee.routine.UserInfo;
import com.gensee.view.GSVideoView;
import com.sp.lib.common.util.ContextUtil;

public class LiveManager implements RtComp.Callback{
    RtSimpleImpl simpleImpl;
    private Activity context;
    UserInfo self;
    GSVideoView gsVideoView;

    public LiveManager(Activity context, GSVideoView gsVideoView) {
        this.context = context;
        this.gsVideoView = gsVideoView;
        simpleImpl=new SimImpl();
        simpleImpl.setVideoView(gsVideoView);
    }

    public void startPlay(String joinPwd,String nickName,String liveId) {
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
        if (self == null) {
            return true;
        }
        simpleImpl.leave(false);
        return false;
    }



    @Override
    public void onInited(String s) {
        simpleImpl.joinWithParam("", s);
    }

    @Override
    public void onErr(int i) {

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

                    String resultDesc = null;
                    switch (result) {
                        //加入成功  除了成功其他均需要正常提示给用户
                        case IRTEvent.IRoomEvent.JoinResult.JR_OK:
                            resultDesc = "您已加入成功";
                            break;
                        //加入错误
                        case IRTEvent.IRoomEvent.JoinResult.JR_ERROR:
                            resultDesc = "加入失败，重试或联系管理员";
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
                    ContextUtil.toast(resultDesc);
                }
            });
        }

        @Override
        public void onRoomLeave(int reason) {
            super.onRoomLeave(reason);
            self=null;
        }

        @Override
        public Context onGetContext() {
            return context.getBaseContext();
        }
    }
}
