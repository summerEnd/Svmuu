package com.svmuu.common;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;

import com.gensee.common.ServiceType;
import com.gensee.download.ErrorCode;
import com.gensee.download.VodDownLoadEntity;
import com.gensee.download.VodDownLoader;
import com.gensee.entity.ChatMsg;
import com.gensee.entity.DocInfo;
import com.gensee.entity.InitParam;
import com.gensee.entity.QAMsg;
import com.gensee.entity.VodObject;
import com.gensee.entity.VodParam;
import com.gensee.media.VODPlayer;
import com.gensee.utils.StringUtil;
import com.gensee.view.GSVideoView;
import com.gensee.vod.VodSite;
import com.sp.lib.common.util.ContextUtil;

import java.util.List;


public class VodManager {
    private int DOWNLOAD_PAUSE_OR_PLAY = 0;
    private Activity context;
    private GSVideoView videoView;
    private VodDownLoader mVodDownLoad;
    private List<VodDownLoadEntity> loadEntities;

    private VODPlayer mGSOLPlayer;

    public static VodManager getInstance(Activity context,GSVideoView videoView){
        VodManager manager=new VodManager();
        manager.setVideoView(videoView);
        manager.setContext(context);
        return manager;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    private VodManager() {
    }

    public void start(String vodId, String password) {
        InitParam initParam = new InitParam();
        // domain 域名
        initParam.setDomain("svmuu.gensee.com");
        // 点播编号 （不是点播id）
        //initParam.setNumber(strNumber);
        // 设置点播id，和点播编号对应，两者至少要有一个有效才能保证成功
//        initParam.setLiveId("30d998bdd65b4a2bb6c405cad9d8dee5");
        initParam.setLiveId(vodId);
        // 站点认证帐号
        initParam.setLoginAccount("admin@svmuu.com");
        // 站点认证密码
        initParam.setLoginPwd("888888");
        // 点播口令
        initParam.setVodPwd(password);
        // 昵称 用于统计和显示
        initParam.setNickName("lincoln");
        // 服务类型（站点类型）
        // webcast - ST_CASTLINE
        // training - ST_TRAINING
        // meeting - ST_MEETING
        initParam.setServiceType(ServiceType.ST_CASTLINE);

        //使用点播编号或点播id来获取详细信息
        VodSite vod = new VodSite(context);
        vod.setVodListener(new VodL());
        vod.getVodObject(initParam);

    }

    public interface RESULT {
        int BROSTCAST_RECEIVE = 1;
        int DOWNLOAD_ERROR = 2;
        int DOWNLOAD_STOP = 3;
        int INITOFFLINE_COMP = 4;
        int DOWNLOAD_START = 5;
        int ON_GET_VODOBJ = 100;
    }

    class VodL implements VodSite.OnVodListener {
        @Override
        public void onChatHistory(String s, List<ChatMsg> list) {

        }

        @Override
        public void onQaHistory(String s, List<QAMsg> list) {

        }

        @Override
        public void onVodErr(int i) {

        }

        @Override
        public void onVodObject(String s) {
            mHandler.sendMessage(mHandler.obtainMessage(RESULT.ON_GET_VODOBJ, s));

        }

        @Override
        public void onVodDetail(VodObject vodObject) {

        }
    }

    class DCallback implements VodDownLoader.OnDownloadListener {
        @Override
        public void onDLFinish(String s, String s1) {

        }

        @Override
        public void onDLPrepare(String s) {

        }

        @Override
        public void onDLPosition(String s, int i) {

        }

        @Override
        public void onDLStart(String s) {

        }

        @Override
        public void onDLStop(String s) {

        }

        @Override
        public void onDLError(String s, int i) {

        }
    }

    private void download(final String vodId) {
        if (null == mVodDownLoad) {
            mVodDownLoad = VodDownLoader.instance(context.getApplicationContext(),
                    new DCallback(), null);
        }
        //		mVodDownLoad.add(mVodParam);
        mVodDownLoad.setAutoDownloadNext(true);
        //		int ret = mVodDownLoad.start(mVodParam.getVodId());
        int ret = mVodDownLoad.download(vodId);

        switch (ret) {
            case ErrorCode.DOWNLOADING_HAVE_EXIST:
                ContextUtil.toast("当前已有下载任务 。目前的机制是单任务下载"
                );
                break;
            case ErrorCode.DOWNLOADING_URL_NULL:
                ContextUtil.toast("下载地址为空");
                break;
            case ErrorCode.OBJECT_HAVE_EXIST:
                ContextUtil.toast("录制件已在下载队列中");
                break;
            case ErrorCode.OBJECT_IS_NULL:
                ContextUtil.toast("传入参数为空");
                break;
            case ErrorCode.OBJECT_NOT_EXIST:
                ContextUtil.toast("目标不存在");
                break;
            case ErrorCode.SDCARD_ERROR:
                ContextUtil.toast("SD卡异常");
                break;
            case ErrorCode.SUCCESS:
                ContextUtil.toast("下载开始");
                break;
            default:
                break;
        }

        loadEntities = mVodDownLoad.getDownloadList();
    }

    private class PlayListener implements VODPlayer.OnVodPlayListener{
        @Override
        public void onInit(int i, boolean b, int i1, List<DocInfo> list) {

        }

        @Override
        public void onPlayStop() {

        }

        @Override
        public void onPlayPause() {

        }

        @Override
        public void onPlayResume() {

        }

        @Override
        public void onPosition(int i) {

        }

        @Override
        public void onVideoSize(int position, int w, int h) {
            Message msg = mHandler.obtainMessage(MSG.MSG_ON_VIDEO_SIZE, 0);
            msg.arg1=w;
            msg.arg2=h;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onPage(int i) {

        }

        @Override
        public void onSeek(int i) {

        }

        @Override
        public void onAudioLevel(int i) {

        }

        @Override
        public void onCaching(boolean b) {

        }

        @Override
        public void onError(int i) {

        }
    }

    public void setVideoView(GSVideoView videoView) {
        this.videoView = videoView;
    }

    private void initPlayer(GSVideoView videoView,String vodId,String localpath) {

        String vodIdOrLocalPath = null;
        if(!StringUtil.isEmpty(localpath)){
            vodIdOrLocalPath = localpath;
        }else if(!StringUtil.isEmpty(vodId)){
            vodIdOrLocalPath = vodId;
        }
        if (vodIdOrLocalPath == null) {
            ContextUtil.toast("路径不对");
            return;
        }
        if (mGSOLPlayer == null) {
            mGSOLPlayer = new VODPlayer();
            mGSOLPlayer.setGSVideoView(videoView);
//            mGSOLPlayer.setDocView(mDocView);
            mGSOLPlayer.play(vodIdOrLocalPath, new PlayListener(), "");
        }
    }

    public void release() {
        stopPlay();
        if (mGSOLPlayer != null) {
            mGSOLPlayer.release();
        }
    }
    public void stopPlay() {
        if (mGSOLPlayer != null) {
            mGSOLPlayer.stop();
        }
    }
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {

                case RESULT.DOWNLOAD_ERROR:

                    ContextUtil.toast("下载出错");
                    break;
                case RESULT.DOWNLOAD_STOP:
                    DOWNLOAD_PAUSE_OR_PLAY = 1;
                    ContextUtil.toast("下载停止");

                    break;
                case RESULT.DOWNLOAD_START:
                    DOWNLOAD_PAUSE_OR_PLAY = 0;
                    ContextUtil.toast("下载开始");
                    break;
                case RESULT.BROSTCAST_RECEIVE:

                    // if (null == mVodDownLoad) {
                    // mVodDownLoad = VodDownLoad.initVodDownLoad(
                    // getApplicationContext(), MainActivity.this);
                    // }
                    final VodParam mVodParam = (VodParam) msg.obj;
                    //				download(mVodParam);
                    // mVodDownLoad.add(mVodParam);
                    // // mVodDownLoad.setAutoDownloading(true);
                    // int ret = mVodDownLoad.start(mVodParam.getVodId());
                    initPlayer(videoView,mVodParam.getVodId(),"");
                    // 进行在线播放

                    //                    Intent i = new Intent(VodActivity.this,
                    //                            PlayActivity.class);
                    //                    i.putExtra("play_param", mVodParam);
                    //                    startActivity(i);
                    // 进行下载
                    // download(mVodParam.getVodId());
                    break;
                case RESULT.ON_GET_VODOBJ:

                    final String vodId = (String) msg.obj;
                    // 进行在线播放
                    initPlayer(videoView,vodId,"");
                    // 进行下载

                    //                    download(vodId);


                    break;
                case RESULT.INITOFFLINE_COMP:

                    if (mVodDownLoad == null) {
                        mVodDownLoad = VodDownLoader.instance(context, new DCallback(), null);
                    }
                    loadEntities = mVodDownLoad.getDownloadList();

                    if (loadEntities != null && loadEntities.size() != 0) {
                    }
                    break;
                case MSG.MSG_ON_VIDEO_SIZE:{
                    //                    int w=msg.arg1;
                    //                    int h=msg.arg2;
                    //                    if (videoView!=null){
                    //                        float ratio=h/(float)w;
                    //                        ViewGroup.LayoutParams lp = videoView.getLayoutParams();
                    //                        lp.height= (int) (videoView.getMeasuredWidth()*ratio);
                    //                    }
                    //                    videoView.requestLayout();
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });
}
