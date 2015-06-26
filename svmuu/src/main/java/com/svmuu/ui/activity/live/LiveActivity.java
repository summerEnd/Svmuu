package com.svmuu.ui.activity.live;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gensee.view.GSVideoView;
import com.svmuu.R;
import com.svmuu.common.LiveManager;
import com.svmuu.common.Tests;
import com.svmuu.ui.BaseActivity;
import com.svmuu.ui.pop.YAlertDialog;

public class LiveActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

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
    ChartFragment chartFragment = new ChartFragment();
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
    Tests.LiveInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        gsView = (GSVideoView) findViewById(R.id.gsView);

        initialize();
    }

    @Override
    public void onBackPressed() {
        manager.tryRelease();
        WHAT = CLOSE;

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

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.check(R.id.liveRoom);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.fullScreen).setOnClickListener(this);
        showVideoView(true);
        manager.setUp(this, gsView, new LiveManager.Callback() {
            @Override
            public void onResult(boolean success) {
//                findViewById(R.id.no_live).setVisibility(success ? View.GONE : View.VISIBLE);
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

    @Override
    protected void onResume() {
        super.onResume();


        new Tests.GetAvailableLive() {
            @Override
            public void onResult(Tests.LiveInfo info) {
                mInfo = info;
                if (WHAT!=0){
                    setMode(WHAT);
                }else {
                    manager.startPlay(info.attendeeToken,"LINCOLN",info.id);
                }
            }
        }.get();
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
                displayFragment(chartFragment);
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
}
