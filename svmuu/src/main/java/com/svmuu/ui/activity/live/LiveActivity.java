package com.svmuu.ui.activity.live;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gensee.view.GSVideoView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.common.util.SLog;
import com.svmuu.AppDelegate;
import com.svmuu.R;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.LiveManager;
import com.svmuu.common.VodManager;
import com.svmuu.common.http.HttpHandler;
import com.svmuu.common.http.HttpManager;
import com.svmuu.common.http.Response;
import com.svmuu.ui.BaseActivity;
import com.svmuu.ui.pop.ProgressIDialog;
import com.svmuu.ui.pop.YAlertDialog;

import org.apache.http.Header;
import org.json.JSONException;

@SuppressWarnings("unused")
public class LiveActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    public static final String EXTRA_QUANZHU_ID = "quanzhu_id";
    public static final String EXTRA_VOD_ID = "vod_id";

    private final int FULL_SCREEN = -1;
    private final int AUDIO = LiveManager.MODE_AUDIO;
    private final int TEXT = LiveManager.MODE_TEXT;
    private final int VIDEO = LiveManager.MODE_VIDEO;
    //关闭页面
    private final int CLOSE = -2;
    //切换为录像
    private final int TO_VIDEO = -3;
    private int WHAT = 0;//初始状态
    LiveManager liveManager;
    VodManager vodManager;
    private GSVideoView gsView;
    VideoListFragment videoListFragment;
    BoxFragment boxFragment = new BoxFragment();
    ChatFragment chatFragment;
    Fragment curFragment;
    private TextView popularity;
    private ImageView menuIcon;
    private ImageView concern;
    private TextView subject;
    private TextView circleName;
    private TextView fansNumber;
    private ImageView indicator;
    private TextView masterName;
    private ImageView avatarImage;
    private TextView textLive;
    private LiveModeSelector modeSelector;
    private boolean isCare = false;
    LiveInfo mInfo;
    _DATA data;
    private String vodId;
    private String vodPsw;

    ProgressIDialog progressIDialog;

    private boolean isVod = false;
    //liveManager回调
    private LiveManager.Callback liveManagerCallback = new LiveManager.Callback() {
        @Override
        public void onResult(boolean success) {
            //findViewById(R.id.no_live).setVisibility(success ? View.GONE : View.VISIBLE);
        }

        @Override
        public void onLeaveRoom(String msg) {
            //这里可以弹出对话框，确定后在关闭界面
            if (progressIDialog != null) {
                progressIDialog.dismiss();
            }
            switch (WHAT) {
                case FULL_SCREEN: {
                    toFullScreen();
                    break;
                }
                case AUDIO: {
                    if (mInfo != null) {
                        gsView.renderDefault();
                        liveManager.setGSView(null);
                        liveManager.startPlay(mInfo.guest_token, "LINCOLN", mInfo.id);
                    }
                    break;
                }
                case VIDEO: {
                    if (mInfo != null) {
                        liveManager.setGSView(gsView);
                        liveManager.startPlay(mInfo.guest_token, "LINCOLN", mInfo.id);
                    }
                    break;
                }
                case TEXT: {
                    if (mInfo != null) {
                        liveManager.setGSView(null);
                    }
                    break;
                }
                case CLOSE: {
                    finish();
                    break;
                }
                case TO_VIDEO: {
                    isVod = true;
                    vodManager.start(vodId, vodPsw);
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        gsView = (GSVideoView) findViewById(R.id.gsView);

        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isVod){
            vodManager.start(vodId,vodPsw);
        }else{
            liveManager.setUp(this,gsView,liveManagerCallback);
        }
    }

    private void initialize() {
        liveManager = LiveManager.getInstance();
        gsView = (GSVideoView) findViewById(R.id.gsView);
        popularity = (TextView) findViewById(R.id.popularity);
        concern = (ImageView) findViewById(R.id.concern);
        subject = (TextView) findViewById(R.id.subject);
        fansNumber = (TextView) findViewById(R.id.fansNumber);
        masterName = (TextView) findViewById(R.id.masterName);
        circleName = (TextView) findViewById(R.id.circleName);
        textLive = (TextView) findViewById(R.id.textLive);
        avatarImage = (ImageView) findViewById(R.id.avatarImage);
        concern.setOnClickListener(this);

        View changeLiveType = findViewById(R.id.changeLiveType);
        indicator = (ImageView) changeLiveType.findViewById(R.id.indicator);
        menuIcon = (ImageView) changeLiveType.findViewById(R.id.menuIcon);
        changeLiveType.setOnClickListener(this);


        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.fullScreen).setOnClickListener(this);
        showVideoView(true);
        //创建聊天
        String circleId = getIntent().getStringExtra(EXTRA_QUANZHU_ID);
        if (TextUtils.isEmpty(circleId)) {
            YAlertDialog.show(this, getString(R.string.live_not_exist)).setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
        }
        chatFragment = ChatFragment.newInstance(circleId);
        videoListFragment = VideoListFragment.newInstance(circleId);
        //选中第一个tab
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.check(R.id.liveRoom);
        liveManager.setUp(this, gsView, liveManagerCallback);
        setLiveInfo(null);
        getLiveInfo(circleId);
    }

    @Override
    public void onBackPressed() {
        if (vodManager != null) {
            vodManager.release();
        }
        if (liveManager.tryRelease()) {
            super.onBackPressed();
        } else {
            WHAT = CLOSE;
            ProgressIDialog.show(this, "正在退出直播...");
        }

    }

    public void getLiveInfo(String circleId) {
        SRequest request = new SRequest("livevideo");
        request.put("quanzhu_id", circleId);
        HttpManager.getInstance().postMobileApi(this, request, new HttpHandler() {
            @Override
            public void onResultOk(int statusCode, Header[] headers, Response response) throws JSONException {
                data = JsonUtil.get(response.data, _DATA.class);
                mInfo = data.liveInfo;
                setLiveInfo(data.user_info);
            }
        });
    }

    void showVideoView(boolean show) {
        if (show) {
            textLive.setVisibility(View.GONE);
            findViewById(R.id.videoLayout).setVisibility(View.VISIBLE);
        } else {
            textLive.setVisibility(View.VISIBLE);
            findViewById(R.id.videoLayout).setVisibility(View.GONE);
        }
    }

    void setLiveInfo(UserInfo userInfo) {
        String fans;
        String hot;
        String unick;
        String live_subject;
        String uface;

        if (userInfo != null) {
            fans = userInfo.fans;
            hot = "0";
            unick = userInfo.unick;
            live_subject = userInfo.live_subject;
            uface = userInfo.uface;
            LiveInfo info = data.liveInfo;
            if (info != null) {
                liveManager.startPlay(info.guest_token, unick, info.zb_id);
            }
        } else {
            fans = "0";
            hot = "0";
            unick = "";
            live_subject = "";
            uface = "";
        }
        fansNumber.setText(getString(R.string.fans_s, fans));
        popularity.setText(getString(R.string.popularity_s, hot));
        masterName.setText(unick);
        circleName.setText(unick);
        subject.setText(live_subject);
        ImageLoader.getInstance().displayImage(uface, avatarImage, ImageOptions.getRoundCorner(6));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeLiveType: {
                createPopIfNeed();

                if (modeSelector.isShowing()) {
                    modeSelector.dismiss();
                    rotate(false);
                } else {
                    modeSelector.showAsDropDown(v);
                    rotate(true);
                }

                break;
            }
            case R.id.concern: {
                if (isCare) {
                    concern.setImageResource(R.drawable.care_normal);
                } else {
                    concern.setImageResource(R.drawable.care_checked);
                }
                isCare = !isCare;
                break;
            }
            case R.id.fullScreen: {
                if (vodManager!=null){
                    vodManager.release();
                }
                if (liveManager.leaveCast()) {
                    toFullScreen();
                } else {
                    showSwitchDialog("切换中...");
                    WHAT = FULL_SCREEN;
                }

                break;
            }
            case R.id.back: {
                onBackPressed();
                break;
            }

        }
    }

    private void toFullScreen() {

        String liveId;
        String token;
        String nick = AppDelegate.getInstance().getUser().name;
        if (isVod) {
            liveId = vodId;
            token = vodPsw;
        } else {
            if (mInfo == null) {
                return;
            }
            liveId = mInfo.id;
            token = mInfo.guest_token;
        }
        startActivity(new Intent(this, FullScreenVideo.class)
                        .putExtra(FullScreenVideo.EXTRA_JOIN_TOKEN, token)
                        .putExtra(FullScreenVideo.EXTRA_NICK_NAME, nick)
                        .putExtra(FullScreenVideo.EXTRA_LIVE_ID, liveId)
                        .putExtra(FullScreenVideo.EXTRA_IS_VOD, isVod)
        );
    }

    private void createPopIfNeed() {
        if (modeSelector == null) {
            modeSelector = new LiveModeSelector(this) {
                @Override
                public void onModePick(int mode) {
                    setMode(mode);
                    dismiss();
                }
            };

            modeSelector.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    rotate(false);
                }

            });
        }
    }

    private void setMode(int mode) {
        switch (mode) {
            case LiveManager.MODE_VIDEO:
                showVideoView(true);
                menuIcon.setImageResource(R.drawable.mode_video_normal);
                WHAT = VIDEO;
                break;
            case LiveManager.MODE_AUDIO:
                showVideoView(true);
                menuIcon.setImageResource(R.drawable.mode_audio_normal);
                WHAT = AUDIO;
                break;
            case LiveManager.MODE_TEXT:
                showVideoView(false);
                menuIcon.setImageResource(R.drawable.mode_text_normal);
                WHAT = TEXT;
                break;
        }
        liveManager.tryRelease();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.liveRoom: {
                displayFragment(chatFragment);
                break;
            }
            case R.id.video: {
                displayFragment(videoListFragment);
                break;
            }
            case R.id.box: {
                displayFragment(boxFragment);
                break;
            }
        }
    }

    void displayFragment(Fragment fragment) {

        if (fragment == curFragment) {
            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (!fragment.isAdded()) {
            ft.add(R.id.fragmentContainer, fragment);
        } else {
            ft.show(fragment);
        }

        if (curFragment != null) {
            ft.hide(curFragment);
        }

        ft.commit();
        curFragment = fragment;
    }

    void rotate(boolean showMenu) {
        float fromD;
        float toD;
        if (showMenu) {
            fromD = 0;
            toD = -180;
        } else {
            fromD = -180;
            toD = 0;
        }
        RotateAnimation animation = new RotateAnimation(fromD, toD, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        indicator.startAnimation(animation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //直播转录像
    public void L2V(String vodId, String psw) {
        SLog.debug_format("vodId:%s psw:%s",vodId,psw);
        this.vodId = vodId;
        this.vodPsw = psw;
        if (vodManager == null) {
            vodManager = VodManager.getInstance(this, gsView);
        }

        if (liveManager.tryRelease()) {
            vodManager.start(vodId, psw);
        } else {
            WHAT = TO_VIDEO;
            showSwitchDialog("正在打开录像...");
        }


    }

    private void showSwitchDialog(String message) {
        if (progressIDialog == null) {
            progressIDialog = new ProgressIDialog(this);
        } else {
            if (progressIDialog.isShowing()) {
                progressIDialog.dismiss();
            }
        }
        progressIDialog.setMessage(message);
        progressIDialog.show();
    }

    /**
     * 录像转直播
     */
    public void V2L(String pwd, String nick, String liveId) {
        vodManager.release();
        liveManager.startPlay(pwd, nick, liveId);
        showSwitchDialog("正在打开直播...");
    }

    private class _DATA {
        UserInfo user_info;
        LiveInfo liveInfo;
    }

    private class LiveInfo {

        public String uid;
        public String id;
        public String zb_url;
        public String box_id;
        public String zb_number;
        public String zb_id;
        public String zb_token;
        public String guest_token;
        public String admin_token;
        public String guest_url;
        public String admin_url;


    }

    private class UserInfo {
        public String uid;
        public String isQuanzhu;
        public String fans;
        public String live_subject;
        public String adminFlag;
        public String uface;
        public String unick;
    }
}
