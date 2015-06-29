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
import com.svmuu.R;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.LiveManager;
import com.svmuu.common.http.HttpHandler;
import com.svmuu.common.http.HttpManager;
import com.svmuu.common.http.Response;
import com.svmuu.ui.BaseActivity;
import com.svmuu.ui.pop.YAlertDialog;

import org.apache.http.Header;
import org.json.JSONException;

@SuppressWarnings("unused")
public class LiveActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    public static final String EXTRA_QUANZHU_ID = "id";
    private final int FULL_SCREEN = -1;
    private final int AUDIO = LiveManager.MODE_AUDIO;
    private final int TEXT = LiveManager.MODE_TEXT;
    private final int VIDEO = LiveManager.MODE_VIDEO;
    private final int CLOSE = -2;
    private int WHAT = 0;//初始状态
    LiveManager manager;
    private GSVideoView gsView;
    VideoFragment videoFragment = new VideoFragment();
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

    //liveManager回调
    private LiveManager.Callback liveManagerCallback = new LiveManager.Callback() {
        @Override
        public void onResult(boolean success) {
            //findViewById(R.id.no_live).setVisibility(success ? View.GONE : View.VISIBLE);
        }

        @Override
        public void onLeaveRoom(String msg) {
            //这里可以弹出对话框，确定后在关闭界面

            switch (WHAT) {
                case FULL_SCREEN: {
                    toFullScreen();
                    break;
                }
                case AUDIO: {
                    if (mInfo != null) {
                        gsView.renderDefault();
                        manager.setGSView(null);
                        manager.startPlay(mInfo.attendeeToken, "LINCOLN", mInfo.id);
                    }
                    break;
                }
                case VIDEO: {
                    if (mInfo != null) {
                        manager.setGSView(gsView);
                        manager.startPlay(mInfo.attendeeToken, "LINCOLN", mInfo.id);
                    }
                    break;
                }
                case TEXT: {
                    if (mInfo != null) {
                        manager.setGSView(null);
                    }
                    break;
                }
                case CLOSE: {
                    finish();
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


    private void initialize() {
        manager = LiveManager.getInstance();

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
        //选中第一个tab
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.check(R.id.liveRoom);
        manager.setUp(this, gsView, liveManagerCallback);
        setLiveInfo(null);
        getLiveInfo(circleId);
    }

    @Override
    public void onBackPressed() {
        if (manager.tryRelease()) {
            super.onBackPressed();
        } else {
            WHAT = CLOSE;
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
                setLiveInfo(mInfo);
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

    void setLiveInfo(LiveInfo info) {
        String fans ;
        String hot ;
        String unick ;
        String live_subject ;
        String uface ;

        if (info != null) {
            fans = info.fans;
            hot = "0";
            unick = info.unick;
            live_subject = info.live_subject;
            uface = info.uface;

        } else {
            fans="0";
            hot="0";
            unick="";
            live_subject="";
            uface="";
        }
        fansNumber.setText(getString(R.string.fans_s, fans));
        popularity.setText(getString(R.string.popularity_s, hot));
        masterName.setText(unick);
        circleName.setText(unick);
        subject.setText(live_subject);
        ImageLoader.getInstance().displayImage(uface, avatarImage, ImageOptions.getRoundCorner(6));
    }

    @Override
    protected void onResume() {
        super.onResume();
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

                if (manager.leaveCast()) {
                    toFullScreen();
                } else {
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
        if (mInfo == null) {
            return;
        }
        startActivity(new Intent(this, FullScreenVideo.class)
                        .putExtra(FullScreenVideo.EXTRA_JOIN_TOKEN, mInfo.attendeeToken)
                        .putExtra(FullScreenVideo.EXTRA_NICK_NAME, "lincoln")
                        .putExtra(FullScreenVideo.EXTRA_LIVE_ID, mInfo.id)
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
        manager.tryRelease();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.liveRoom: {
                displayFragment(chatFragment);
                break;
            }
            case R.id.video: {
                displayFragment(videoFragment);
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


    private class _DATA {
        UserInfo user_info;
        LiveInfo liveInfo;
    }

    private class LiveInfo {
        public String uid;
        public String isQuanzhu;
        public String fans;
        public String live_subject;
        public String adminFlag;
        public String uface;
        public String unick;

        //自己添加
        public String attendeeToken;
        public String id;
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
