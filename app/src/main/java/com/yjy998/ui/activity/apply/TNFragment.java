package com.yjy998.ui.activity.apply;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;
import com.yjy998.ui.activity.BaseFragment;
import com.yjy998.ui.view.CircleItem;

public class TNFragment extends BaseFragment implements View.OnClickListener {


    private CircleItem circle0;
    private CircleItem circle1;
    private CircleItem circle2;
    private CircleItem circle3;
    private CircleItem circle4;
    private CircleItem circle5;
    private TextView totalCapitalText;
    private TextView manageFeeText;
    private ImageView questionImage;
    private TextView pingCangText;
    private TextView traderText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tn, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initialize();
    }


    @Override
    public String getTitle() {
        return ContextUtil.getString(R.string.tn);
    }

    private void initialize() {

        circle0 = (CircleItem) findViewById(R.id.circle0);
        circle1 = (CircleItem) findViewById(R.id.circle1);
        circle2 = (CircleItem) findViewById(R.id.circle2);

        circle3 = (CircleItem) findViewById(R.id.circle3);
        circle4 = (CircleItem) findViewById(R.id.circle4);
        circle5 = (CircleItem) findViewById(R.id.circle5);
        findViewById(R.id.changeGroup).setOnClickListener(this);
        findViewById(R.id.introduce).setOnClickListener(this);
        totalCapitalText = (TextView) findViewById(R.id.totalCapitalText);
        manageFeeText = (TextView) findViewById(R.id.manageFeeText);
        questionImage = (ImageView) findViewById(R.id.questionImage);
        pingCangText = (TextView) findViewById(R.id.pingCangText);
        traderText = (TextView) findViewById(R.id.traderText);
    }

    @Override
    public void onClick(View v) {

    }
}
