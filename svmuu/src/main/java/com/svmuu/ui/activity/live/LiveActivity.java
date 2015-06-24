package com.svmuu.ui.activity.live;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
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
import com.svmuu.ui.BaseActivity;
import com.svmuu.ui.activity.FullScreenVideo;

public class LiveActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
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
    private LiveModeSelector modeSelector;
    private boolean isCare=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        gsView = (GSVideoView) findViewById(R.id.gsView);
        manager = new LiveManager(this, gsView);
        initialize();
    }

    @Override
    public void onBackPressed() {
        if (manager.leaveCast()) {
            super.onBackPressed();
        }
    }

    private void initialize() {

        gsView = (GSVideoView) findViewById(R.id.gsView);
        popularity = (TextView) findViewById(R.id.popularity);
        concern = (ImageView) findViewById(R.id.concern);
        subject = (TextView) findViewById(R.id.subject);
        fansNumber = (TextView) findViewById(R.id.fansNumber);
        masterName = (TextView) findViewById(R.id.masterName);
        circleName = (TextView) findViewById(R.id.circleName);
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
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
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
            case R.id.concern:{
                if (isCare){
                    concern.setImageResource(R.drawable.care_normal);
                }else{
                    concern.setImageResource(R.drawable.care_checked);
                }
                isCare=!isCare;
                break;
            }
            case R.id.fullScreen:{
                startActivity(new Intent(this, FullScreenVideo.class));
                break;
            }
        }
    }

    //请求关注
    void requestConcern(){

    }

    private void createPopIfNeed() {
        if (modeSelector == null) {
            modeSelector = new LiveModeSelector(this) {
                @Override
                public void onModePick(int mode) {
                    switch (mode) {
                        case LiveModeSelector.MODE_VIDEO:
                            menuIcon.setImageResource(R.drawable.mode_video_normal);
                            break;
                        case LiveModeSelector.MODE_AUDIO:
                            menuIcon.setImageResource(R.drawable.mode_audio_normal);
                            break;
                        case LiveModeSelector.MODE_TEXT:
                            menuIcon.setImageResource(R.drawable.mode_text_normal);
                            break;
                    }
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
}
