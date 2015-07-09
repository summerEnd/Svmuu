package com.svmuu.common.video;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;

import com.gensee.common.ServiceType;
import com.gensee.entity.InitParam;
import com.gensee.media.VODPlayer;
import com.gensee.utils.StringUtil;
import com.gensee.view.GSVideoView;
import com.gensee.vod.VodSite;
import com.sp.lib.common.util.ContextUtil;
import com.svmuu.common.MSG;

/**
 * 点播
 */
public class VodManager extends AbsVideoManager {

    private static VodManager manager;
    private final VODPlayer mGSOLPlayer;
    private GSVideoView videoView;
    private boolean adjustVideoSize;
    private boolean isPlaying;

    public static VodManager getInstance(Activity activity) {
        if (manager == null) {
            manager = new VodManager(activity);
        }
        return manager;
    }

    private VodManager(Activity activity) {
        setContext(activity);
        mGSOLPlayer = new VODPlayer();
    }

    @Override
    public void setGSView(GSVideoView gsView) {
        this.videoView = gsView;
        mGSOLPlayer.setGSVideoView(gsView);
    }

    @Override
    protected boolean onRelease() {
        mGSOLPlayer.stop();
        isPlaying = false;

        return mGSOLPlayer.release();
    }

    @Override
    protected void onStart(String id, String token) {

        InitParam initParam = new InitParam();
        // domain 域名
        initParam.setDomain("svmuu.gensee.com");
        // 点播编号 （不是点播id）
        //initParam.setNumber(strNumber);
        // 设置点播id，和点播编号对应，两者至少要有一个有效才能保证成功
        //        initParam.setLiveId("30d998bdd65b4a2bb6c405cad9d8dee5");
        initParam.setLiveId(id);
        // 站点认证帐号
        initParam.setLoginAccount("admin@svmuu.com");
        // 站点认证密码
        initParam.setLoginPwd("888888");
        // 点播口令
        initParam.setVodPwd(token);
        // 昵称 用于统计和显示
        initParam.setNickName(getNickName());
        // 服务类型（站点类型）
        // webcast - ST_CASTLINE
        // training - ST_TRAINING
        // meeting - ST_MEETING
        initParam.setServiceType(ServiceType.ST_CASTLINE);
        //使用点播编号或点播id来获取详细信息
        VodSite vod = new VodSite(getContext());
        vod.setVodListener(new VodSiteListener() {

            @Override
            public void onVodObject(String s) {
                mHandler.sendMessage(mHandler.obtainMessage(MSG.ON_GET_VODOBJ, s));

            }
        });
        vod.getVodObject(initParam);


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

    void initPlayer(String id) {
        String localpath = "";//本地视频路径
        String vodIdOrLocalPath = null;
        if (!StringUtil.isEmpty(localpath)) {
            vodIdOrLocalPath = localpath;
        } else if (!StringUtil.isEmpty(id)) {
            vodIdOrLocalPath = id;
        }
        if (vodIdOrLocalPath == null) {
            ContextUtil.toast("路径不对");
            return;
        }

        release();
        isPlaying = true;
        mGSOLPlayer.play(id, new VodListener() {
            @Override
            public void onVideoSize(int position, int w, int h) {
                Message msg = mHandler.obtainMessage(MSG.MSG_ON_VIDEO_SIZE, 0);
                msg.arg1 = w;
                msg.arg2 = h;
                mHandler.sendMessage(msg);
            }
        }, "");
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {

                case MSG.ON_GET_VODOBJ:
                    final String vodId = (String) msg.obj;
                    initPlayer(vodId);
                    break;

                case MSG.MSG_ON_VIDEO_SIZE: {
                    if (adjustVideoSize) {
                        int w = msg.arg1;
                        int h = msg.arg2;
                        if (videoView != null) {
                            float ratio = h / (float) w;
                            ViewGroup.LayoutParams lp = videoView.getLayoutParams();
                            lp.height = (int) (videoView.getMeasuredWidth() * ratio);
                            videoView.requestLayout();
                        }

                    }

                    break;
                }

            }
            return false;
        }
    });

    public void adjustVideoSize(boolean adjustVideoSize) {
        this.adjustVideoSize = adjustVideoSize;
    }
}
