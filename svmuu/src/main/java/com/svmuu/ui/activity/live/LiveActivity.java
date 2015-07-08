package com.svmuu.ui.activity.live;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
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

import org.apache.http.Header;
import org.json.JSONException;

import java.io.Serializable;

@SuppressWarnings("unused")
public class LiveActivity extends BaseActivity implements OnCheckedChangeListener, PlayFragment.Callback {
    public static final String EXTRA_QUANZHU_ID = "quanzhu_id";
    public static final String EXTRA_VOD_ID = "vod_id";

    VideoListFragment videoListFragment;
    BoxFragment boxFragment = new BoxFragment();
    ChatFragment chatFragment;
    Fragment curFragment;
    private TextView popularity;
    private ImageView menuIcon;
    private CheckedTextView concern;
    private TextView circleName;
    private TextView fansNumber;
    private ImageView indicator;
    private TextView masterName;
    private ImageView avatarImage;
    private LiveModeSelector modeSelector;
    LiveInfo mInfo;
    _DATA data;
    ProgressIDialog progressIDialog;

    private String vodId;
    private String psw;
    private String circleId;
    private RadioGroup radioGroup;
    private View changeLiveType;
    PlayFragment mPlayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        initialize();
    }

    private void initialize() {
        popularity = (TextView) findViewById(R.id.popularity);
        concern = (CheckedTextView) findViewById(R.id.concern);
        fansNumber = (TextView) findViewById(R.id.fansNumber);
        masterName = (TextView) findViewById(R.id.masterName);
        circleName = (TextView) findViewById(R.id.circleName);
        avatarImage = (ImageView) findViewById(R.id.avatarImage);
        concern.setOnClickListener(this);

        changeLiveType = findViewById(R.id.changeLiveType);
        indicator = (ImageView) changeLiveType.findViewById(R.id.indicator);
        menuIcon = (ImageView) changeLiveType.findViewById(R.id.menuIcon);
        changeLiveType.setOnClickListener(this);

        findViewById(R.id.back).setOnClickListener(this);
        //创建聊天
        circleId = getIntent().getStringExtra(EXTRA_QUANZHU_ID);

        chatFragment = ChatFragment.newInstance(circleId);
        videoListFragment = VideoListFragment.newInstance(circleId);
        //选中第一个tab
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);

        //初始化fragment，暂时不添加到activity
        mPlayFragment = new PlayFragment();
        mPlayFragment.setCallback(this);

        setLiveInfo(null);
        getLiveInfo();
    }

    @Override
    public void onBackPressed() {

        if (mPlayFragment.onActivityClose()) {
            super.onBackPressed();
        }

    }

    public void getLiveInfo() {
        SRequest request = new SRequest("livevideo");
        request.put("quanzhu_id", circleId);
        HttpManager.getInstance().postMobileApi(this, request, new HttpHandler() {
            @Override
            public void onResultOk(int statusCode, Header[] headers, Response response) throws JSONException {
                data = JsonUtil.get(response.data, _DATA.class);
                mInfo = data.liveInfo;
                setLiveInfo(data.user_info);
            }

            @Override
            public void onResultError(int statusCOde, Header[] headers, Response response) throws JSONException {
                super.onResultError(statusCOde, headers, response);
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(LiveActivity.this);
                builder.setTitle(R.string.warn);
                builder.setMessage(getString(R.string.get_live_info_failed));
                builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getLiveInfo();
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                builder.create().show();
            }

            @Override
            public void onException() {
                super.onException();
            }
        });
    }

    /**
     * 设置直播信息
     * 根据权限来隐藏聊天室、直播录像等
     */
    void setLiveInfo(UserInfo userInfo) {
        String fans;
        String hot;
        String unick;
        String live_subject;
        String uface;

        //是否关注
        boolean isFollow;
        //是否支持聊天
        boolean supportChat = true;
        //是否支持录像
        boolean supportVideo = true;

        if (userInfo != null) {
            fans = userInfo.fans;
            hot = userInfo.totalhot;
            unick = userInfo.unick;
            live_subject = userInfo.live_subject;
            uface = userInfo.uface;

            if ("-1".equals(userInfo.chat_live)) {
                supportChat = false;
            }
            if ("-1".equals(userInfo.video_live)) {
                supportVideo = false;
            }
            LiveInfo info = data.liveInfo;

            if (info != null) {
                //证券娱乐圈(32)默认显示视频直播,其余默认显示文字直播
                if ("32".equals(circleId)) {
                    setTitleIcon(0);
                } else {
                    setTitleIcon(2);
                }
            }
            isFollow = userInfo.isFollow;
        } else {
            fans = "0";
            hot = "0";
            unick = "";
            live_subject = "";
            uface = "";
            isFollow = false;
        }

        //直播室按钮
        RadioButton chatButton = (RadioButton) radioGroup.findViewById(R.id.liveRoom);
        //直播室分割线
        View div1 = radioGroup.findViewById(R.id.dv_1);
        if (supportChat) {
            chatButton.setVisibility(View.VISIBLE);
            div1.setVisibility(View.VISIBLE);
            radioGroup.check(R.id.liveRoom);
        } else {
            chatButton.setVisibility(View.GONE);
            div1.setVisibility(View.GONE);

        }

        //录像按钮
        RadioButton videoButton = (RadioButton) radioGroup.findViewById(R.id.video);
        //直播室分割线
        View div2 = radioGroup.findViewById(R.id.dv_2);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (supportVideo) {
            videoButton.setVisibility(View.VISIBLE);
            div2.setVisibility(View.VISIBLE);
            changeLiveType.setVisibility(View.VISIBLE);
            mPlayFragment.setSubject(live_subject);
            //防止重复加载
            if (!mPlayFragment.isAdded()) {
                ft.add(R.id.playerContainer, mPlayFragment);
                findViewById(R.id.playerContainer).setVisibility(View.VISIBLE);
            }
        } else {
            videoButton.setVisibility(View.GONE);
            div2.setVisibility(View.GONE);
            changeLiveType.setVisibility(View.INVISIBLE);
            //移除视频窗口
            if (mPlayFragment.isAdded()) {
                ft.remove(mPlayFragment);
                findViewById(R.id.playerContainer).setVisibility(View.GONE);
                radioGroup.check(R.id.box);
            }
        }
        ft.commit();
        getSupportFragmentManager().executePendingTransactions();

        fansNumber.setText(getString(R.string.fans_s, fans));
        popularity.setText(getString(R.string.popularity_s, hot));
        masterName.setText(unick);
        circleName.setText(unick);
        concern.setChecked(isFollow);
        ImageLoader.getInstance().displayImage(uface, avatarImage, ImageOptions.getRoundCorner(6));
    }

    void setVideoHeight(int height) {
        View player = findViewById(R.id.playerContainer);
        if (player != null) {
            ViewGroup.LayoutParams lp = player.getLayoutParams();
            if (lp != null) {
                lp.height = height;
            }
            player.requestLayout();
        }
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
                addAttention();
                break;
            }

            case R.id.back: {
                onBackPressed();
                break;
            }

        }
    }

    public void addAttention() {
        SRequest request = new SRequest("attention");
        request.put("groupid", circleId);
        HttpManager.getInstance().postMobileApi(this, request, new HttpHandler() {
            @Override
            public void onResultOk(int statusCode, Header[] headers, Response response) throws JSONException {
                concern.setChecked(true);
            }
        });
    }

    private void createSelectorIfNeed() {
        if (modeSelector == null) {
            modeSelector = new LiveModeSelector(this) {
                @Override
                public void onClicked(int position) {
                    setTitleIcon(position);
                    dismiss();
                }
            };
            modeSelector.check(mPlayFragment.getLiveType());
            modeSelector.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    rotate(false);
                }

            });
        }
    }

    void setTitleIcon(int position) {
        switch (position) {
            case 0: {
                setVideoHeight(getResources().getDimensionPixelSize(R.dimen.dimen_300px));
                menuIcon.setImageResource(R.drawable.ic_mode_video);
                //如果当前直播是语音模式，就不用加载视频
                if (mInfo != null && mPlayFragment.getLiveType() != 1) {
                    mPlayFragment.playLive(mInfo.zb_id, mInfo.zb_token);
                }
                mPlayFragment.setLiveType(0);

                break;
            }
            case 1: {
                menuIcon.setImageResource(R.drawable.ic_mode_audio);
                setVideoHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                //如果当前直播是视频模式，就不用重新加载视频
                if (mInfo != null && mPlayFragment.getLiveType() != 0) {
                    mPlayFragment.playLive(mInfo.zb_id, mInfo.zb_token);
                }
                mPlayFragment.setLiveType(1);
                break;
            }
            case 2: {
                mPlayFragment.setLiveType(2);
                menuIcon.setImageResource(R.drawable.ic_mode_text);
                setVideoHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                break;
            }
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
        _DATA data = this.data;
        if (data == null || data.user_info == null) {
            getLiveInfo();
            return;
        }

        UserInfo user_info = data.user_info;
        startActivity(new Intent(this, BoxDetailActivity.class)
                        .putExtra(BoxDetailActivity.EXTRA_ID, vodId)
                        .putExtra(BoxDetailActivity.EXTRA_TOKEN, psw)
                        .putExtra(BoxDetailActivity.EXTRA_SUBJECT, user_info.live_subject)
                        .putExtra(BoxDetailActivity.EXTRA_CIRCLE_NAME, user_info.unick)
                        .putExtra(BoxDetailActivity.EXTRA_IS_BOX, false)

        );
    }


    /**
     * 录像转直播
     */
    public void V2L(String pwd, String nick, String liveId) {
        mPlayFragment.playLive(liveId, pwd);
    }

    /**
     * 直播关闭后回调
     *
     * @param reason 关闭的原因
     */
    @Override
    public void onReleased(int reason) {

    }


    private class _DATA implements Serializable {
        UserInfo user_info;
        LiveInfo liveInfo;
    }

    private class LiveInfo implements Serializable {

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

    private class UserInfo implements Serializable {
        public String uid;
        public String isQuanzhu;
        public String fans;
        public String totalhot;
        public String live_subject;
        public String adminFlag;
        public String uface;
        public String unick;
        public String chat_live;
        public String video_live;
        public boolean isFollow;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("data", data);
        outState.putSerializable("circleId", circleId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle saveInstance) {
        super.onRestoreInstanceState(saveInstance);
        data = (_DATA) saveInstance.getSerializable("data");
        circleId = saveInstance.getString("circleId");
        if (videoListFragment != null) {
            videoListFragment.setQuanzhu_id(circleId);
            chatFragment.setCircleId(circleId);
        }
    }
}
