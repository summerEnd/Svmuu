package com.svmuu.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sp.lib.common.support.net.client.SRequest;
import com.svmuu.AppDelegate;
import com.svmuu.R;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.entity.User;
import com.svmuu.common.http.HttpHandler;
import com.svmuu.common.http.HttpManager;
import com.svmuu.common.http.Response;
import com.svmuu.ui.BaseFragment;
import com.svmuu.ui.activity.box.BoxActivity;
import com.svmuu.ui.activity.live.LiveActivity;
import com.svmuu.ui.activity.live.MyCircleActivity;
import com.svmuu.ui.activity.settings.SettingActivity;
import com.svmuu.ui.pop.LoginActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuFragment extends BaseFragment implements View.OnClickListener {


    private ImageView avatarImage;
    private TextView phoneText;
    private TextView tvcircleNo;
    private TextView tvShuibao;
    private TextView tvfans;
    private AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recharge: {
                if (dialog == null) {
                    dialog = new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.warn)
                            .setMessage(R.string.function_not_open)
                            .setPositiveButton(R.string.i_know, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                }
                dialog.show();
                break;
            }
            case R.id.myCircle: {
                startActivity(new Intent(getActivity(), MyCircleActivity.class)
                        .putExtra(MyCircleActivity.EXTRA_IS_MY, true));
                break;
            }
            case R.id.myBox: {
                startActivity(new Intent(getActivity(), BoxActivity.class));

                break;
            }
            case R.id.settings: {
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            }
            case R.id.my_circle: {
                User user = AppDelegate.getInstance().getUser();
                String uid = user.uid;
                if (TextUtils.isEmpty(uid)) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }



                startActivity(new Intent(getActivity(), LiveActivity.class)
                        .putExtra(LiveActivity.EXTRA_QUANZHU_ID, uid));
                break;
            }
        }
    }

    public void initialize() {

        avatarImage = (ImageView) findViewById(R.id.avatarImage);
        phoneText = (TextView) findViewById(R.id.phoneText);
        tvcircleNo = (TextView) findViewById(R.id.tv_circleNo);
        tvShuibao = (TextView) findViewById(R.id.tv_Shuibao);
        tvfans = (TextView) findViewById(R.id.tv_fans);
        findViewById(R.id.recharge).setOnClickListener(this);
        findViewById(R.id.myCircle).setOnClickListener(this);
        findViewById(R.id.myBox).setOnClickListener(this);
        findViewById(R.id.settings).setOnClickListener(this);
        findViewById(R.id.my_circle).setOnClickListener(this);
        refreshUI();
        requestRefresh();



        phoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppDelegate.getInstance().isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });
    }

    @Override
    protected void refresh() {
        SRequest request = new SRequest("leftinfo");
        HttpManager.getInstance().postMobileApi(null, request, new HttpHandler() {
            @Override
            public void onResultOk(int statusCOde, Header[] headers, Response response) throws JSONException {
                JSONObject object = new JSONObject(response.data);
                User user = AppDelegate.getInstance().getUser();
                user.fans = object.getString("fans");
                user.money = object.getString("money");
                user.uface = object.getString("uface");
                user.uid = object.getString("uid");
                user.video_live = object.getString("video_live");
                user.chat_live = object.getString("chat_live");
                requestRefreshUI();
            }

            @Override
            public void onResultError(int statusCOde, Header[] headers, Response response) throws JSONException {
                requestRefreshUI();
            }
        });
    }

    @Override
    public void refreshUI() {
        String name;
        String circleNo;
        String shuibao;
        String fans;
        String avatar;
        findViewById(R.id.my_circle).setVisibility(View.VISIBLE);
        if (AppDelegate.getInstance().isLogin()) {
            User user = AppDelegate.getInstance().getUser();
            name = user.name;
            circleNo = user.uid;
            shuibao = user.money;
            fans = user.fans;
            avatar = user.uface;

        } else {
            name = "未登录";
            circleNo = "0";
            shuibao = "0";
            fans = "0";
            avatar = "0";
        }

        phoneText.setText(name);
        tvcircleNo.setText(getString(R.string.circle_no_s, circleNo));
        tvfans.setText(getString(R.string.fans_s, fans));
        tvShuibao.setText(getString(R.string.shui_bao_s, shuibao));
        ImageLoader.getInstance().displayImage(avatar, avatarImage,
                ImageOptions.getRound((int) getResources().getDimension(R.dimen.avatarSize)));
    }

    @Override
    protected void onUserChanged() {
        requestRefresh();
    }
}
