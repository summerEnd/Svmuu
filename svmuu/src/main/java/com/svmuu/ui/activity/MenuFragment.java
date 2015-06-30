package com.svmuu.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.svmuu.ui.pop.YAlertDialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuFragment extends BaseFragment implements View.OnClickListener {


    private OnMenuClick menuClick;
    private ImageView avatarImage;
    private TextView phoneText;
    private TextView tvcircleNo;
    private TextView tvShuibao;
    private TextView tvfans;
    private YAlertDialog dialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnMenuClick) {
            menuClick = (OnMenuClick) activity;
        }
    }

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
                    dialog = YAlertDialog.showNoSuchFunction(getActivity());
                }
                dialog.show();
                break;
            }
            case R.id.myCircle: {
                startActivity(new Intent(getActivity(), MyCircleActivity.class));
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
        requestRefresh();
    }

    @Override
    protected void refresh() {
        SRequest request = new SRequest();
        request.setUrl("/moblieapi/leftinfo");
        HttpManager.getInstance().post(null,request, new HttpHandler() {
            @Override
            public void onResultOk(int statusCOde, Header[] headers, Response response) throws JSONException {
                JSONObject object = new JSONObject(response.data);
                User user = AppDelegate.getInstance().getUser();
                user.fans = object.getString("fans");
                user.money = object.getString("money");
                user.uface = object.getString("uface");
                user.uid = object.getString("uid");
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
        if (AppDelegate.getInstance().isLogin()) {
            User user = AppDelegate.getInstance().getUser();
            name=user.name;
            circleNo=user.uid;
            shuibao=user.money;
            fans=user.fans;
            avatar=user.uface;
        } else {
            name="未登录";
            circleNo="0";
            shuibao="0";
            fans="0";
            avatar="0";
        }

        phoneText.setText(name);
        tvcircleNo.setText(getString(R.string.circle_no_s,circleNo));
        tvfans.setText(getString(R.string.fans_s,fans));
        tvShuibao.setText(getString(R.string.shui_bao_s,shuibao));
        ImageLoader.getInstance().displayImage(avatar, avatarImage,
                ImageOptions.getRound((int) getResources().getDimension(R.dimen.avatarSize)));
    }

    public interface OnMenuClick {

        boolean onMenuClick(View v);
    }
}
