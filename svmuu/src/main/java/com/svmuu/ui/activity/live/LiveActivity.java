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
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.common.util.SLog;
import com.svmuu.R;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.http.HttpHandler;
import com.svmuu.common.http.HttpManager;
import com.svmuu.common.http.Response;
import com.svmuu.ui.BaseActivity;
import com.svmuu.ui.activity.box.BoxDetailActivity;
import com.svmuu.ui.pop.ProgressIDialog;
import com.svmuu.ui.pop.YAlertDialog;

import org.apache.http.Header;
import org.json.JSONException;

@SuppressWarnings("unused")
public class LiveActivity extends BaseActivity implements OnCheckedChangeListener,PlayFragment.Callback {
    public static final String EXTRA_QUANZHU_ID = "quanzhu_id";
    public static final String EXTRA_VOD_ID = "vod_id";

    VideoListFragment videoListFragment;
    BoxFragment boxFragment = new BoxFragment();
    ChatFragment chatFragment;
    Fragment curFragment;
    private TextView popularity;
    private ImageView menuIcon;
    private ImageView concern;
    private TextView circleName;
    private TextView fansNumber;
    private ImageView indicator;
    private TextView masterName;
    private ImageView avatarImage;
    private LiveModeSelector modeSelector;
    private boolean isCare = false;
    LiveInfo mInfo;
    _DATA data;

    ProgressIDialog progressIDialog;


    PlayFragment mPlayFragment;
    private String vodId;
    private String psw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        mPlayFragment = new PlayFragment();
        mPlayFragment.setCallback(this);
        getSupportFragmentManager().beginTransaction().add(R.id.playerContainer, mPlayFragment).commit();
        initialize();
    }

    private void initialize() {

        popularity = (TextView) findViewById(R.id.popularity);
        concern = (ImageView) findViewById(R.id.concern);
        fansNumber = (TextView) findViewById(R.id.fansNumber);
        masterName = (TextView) findViewById(R.id.masterName);
        circleName = (TextView) findViewById(R.id.circleName);
        avatarImage = (ImageView) findViewById(R.id.avatarImage);
        concern.setOnClickListener(this);

        View changeLiveType = findViewById(R.id.changeLiveType);
        indicator = (ImageView) changeLiveType.findViewById(R.id.indicator);
        menuIcon = (ImageView) changeLiveType.findViewById(R.id.menuIcon);
        changeLiveType.setOnClickListener(this);


        findViewById(R.id.back).setOnClickListener(this);
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
        setLiveInfo(null);
        getLiveInfo(circleId);
    }

    @Override
    public void onBackPressed() {

        mPlayFragment.onActivityClose();

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
                mPlayFragment.playLive(info.zb_id, info.zb_token);
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
        mPlayFragment.setSubject(live_subject);
        ImageLoader.getInstance().displayImage(uface, avatarImage, ImageOptions.getRoundCorner(6));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeLiveType: {
                createSelectorIfNeed();

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

            case R.id.back: {
                onBackPressed();
                break;
            }

        }
    }


    private void createSelectorIfNeed() {
        if (modeSelector == null) {
            modeSelector = new LiveModeSelector(this) {
                @Override
                public void onClicked(int position) {

                    switch (position) {
                        case 0: {
                            menuIcon.setImageResource(R.drawable.mode_video_normal);
                            break;
                        }
                        case 1: {
                            menuIcon.setImageResource(R.drawable.mode_audio_normal);

                            break;
                        }
                        case 2: {
                            menuIcon.setImageResource(R.drawable.mode_text_normal);

                            break;
                        }
                    }
                    dismiss();
                    mPlayFragment.setLiveType(position);
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
        this.vodId = vodId;
        this.psw = psw;
        SLog.debug_format("vodId:%s psw:%s", vodId, psw);
        mPlayFragment.stop();
    }



    /**
     * 录像转直播
     */
    public void V2L(String pwd, String nick, String liveId) {
        mPlayFragment.playLive(liveId, pwd);
    }

    /**
     * 直播关闭后回调
     * @param reason 关闭的原因
     */
    @Override
    public void onReleased(int reason) {
        if (reason== PlayFragment.Reason.STOP_PLAY){
            startActivity(new Intent(this, BoxDetailActivity.class)
            .putExtra(BoxDetailActivity.EXTRA_ID,vodId)
            .putExtra(BoxDetailActivity.EXTRA_TOKEN,psw)
            .putExtra(BoxDetailActivity.EXTRA_SUBJECT,data.user_info.live_subject)
            .putExtra(BoxDetailActivity.EXTRA_CIRCLE_NAME,data.user_info.unick)
            .putExtra(BoxDetailActivity.EXTRA_IS_BOX,false)

            );
        }
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
