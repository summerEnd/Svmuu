package com.svmuu.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sp.lib.common.util.ContextUtil;
import com.svmuu.R;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.Tests;
import com.svmuu.ui.BaseFragment;
import com.svmuu.ui.pop.YAlertDialog;

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
                    dialog = new YAlertDialog(getActivity());
                    dialog.setTitle(R.string.warn);
                    dialog.setMessage(ContextUtil.getString(R.string.function_not_open));
                    dialog.setButton(getString(R.string.yes), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.hide();
                        }
                    });
                }
                dialog.show();
                break;
            }
            case R.id.myCircle: {
                startActivity(new Intent(getActivity(),LiveListActivity.class));
                break;
            }
            case R.id.myBox: {
                dialog.dismiss();
                break;
            }
            case R.id.settings: {
                startActivity(new Intent(getActivity(),SettingActivity.class));
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
        refreshUI();
    }

    @Override
    public void refresh() {

    }

    @Override
    public void refreshUI() {
        ImageLoader.getInstance().displayImage(Tests.IMAGE, avatarImage,
                ImageOptions.getRound((int) getResources().getDimension(R.dimen.avatarSize)));
    }

    public interface OnMenuClick {

        boolean onMenuClick(View v);
    }
}
