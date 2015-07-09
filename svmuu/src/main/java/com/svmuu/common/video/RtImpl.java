package com.svmuu.common.video;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.gensee.room.RtSdk;
import com.gensee.room.RtSimpleImpl;
import com.gensee.routine.IRTEvent;
import com.gensee.routine.State;
import com.gensee.routine.UserInfo;
import com.gensee.view.GSVideoView;
import com.sp.lib.common.util.ContextUtil;


public class RtImpl extends RtSimpleImpl {

    Activity context;
    private GSVideoView gsVideoView;

    public RtImpl(Activity context) {
        this.context = context;
    }

    public void setGsVideoView(GSVideoView gsVideoView) {
        this.gsVideoView = gsVideoView;
        setVideoView(gsVideoView);
    }

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
        context.runOnUiThread(new Runnable() {
            public void run() {
                String resultDesc;
                switch (result) {
                    //加入成功  除了成功其他均需要正常提示给用户
                    case IRTEvent.IRoomEvent.JoinResult.JR_OK:
                        resultDesc = "";
                        ContextUtil.toast_debug("-->");
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
                        resultDesc = "";
                        break;
                }
                if (!TextUtils.isEmpty(resultDesc)) ContextUtil.toast(resultDesc);

            }
        });
    }


    /**
     * 直播状态 s.getValue()   0 默认直播未开始 1、直播中， 2、直播停止，3、直播暂停
     */
    @Override
    public void onRoomPublish(State s) {
        super.onRoomPublish(s);
        // 此逻辑是控制视频要在直播开始后才准许看的逻辑
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
            }
        });
    }


    @Override
    public Context onGetContext() {
        return context.getBaseContext();
    }

}
