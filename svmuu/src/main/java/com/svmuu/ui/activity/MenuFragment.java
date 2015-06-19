package com.svmuu.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.svmuu.R;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.Tests;
import com.svmuu.ui.BaseFragment;

public class MenuFragment extends BaseFragment implements View.OnClickListener {


    private OnMenuClick menuClick;
    private ImageView avatarImage;
    private TextView phoneText;
    private TextView tvcircleNo;
    private TextView tvShuibao;
    private TextView tvfans;

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
                break;
            }
            case R.id.myCircle: {
                break;
            }
            case R.id.myBox: {
                break;
            }
            case R.id.settings: {
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
