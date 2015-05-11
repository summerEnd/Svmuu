package com.yjy998.ui.activity.main.apply;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;
import com.yjy998.ui.activity.base.BaseFragment;
import com.yjy998.ui.view.CircleItem;

public class TNFragment extends BaseFragment implements View.OnClickListener {

    private final int W = 10000;
    private int[] DATA = new int[]{
            5000, 1 * W, 3 * W, 5 * W, 10 * W, 20 * W,
            30 * W, 50 * W, 80 * W, 100 * W, 150 * W, 200 * W
    };
    private Item[] items = new Item[12];
    private CircleItem[] circle = new CircleItem[6];
    private int currentGroup = 0;
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

        circle[0] = (CircleItem) findViewById(R.id.circle0);
        circle[1] = (CircleItem) findViewById(R.id.circle1);
        circle[2] = (CircleItem) findViewById(R.id.circle2);
        circle[3] = (CircleItem) findViewById(R.id.circle3);
        circle[4] = (CircleItem) findViewById(R.id.circle4);
        circle[5] = (CircleItem) findViewById(R.id.circle5);
        OnCircleClickListener listener = new OnCircleClickListener();
        for (CircleItem item : circle) {
            item.setOnClickListener(listener);
        }

        findViewById(R.id.changeGroup).setOnClickListener(this);
        findViewById(R.id.introduce).setOnClickListener(this);
        totalCapitalText = (TextView) findViewById(R.id.totalCapitalText);
        manageFeeText = (TextView) findViewById(R.id.manageFeeText);
        questionImage = (ImageView) findViewById(R.id.questionImage);
        pingCangText = (TextView) findViewById(R.id.pingCangText);
        traderText = (TextView) findViewById(R.id.traderText);

        //初始化数据
        for (int i = 0; i < items.length; i++) {
            items[i] = new Item();
            if (DATA[i] < W) {
                items[i].boldText = "";
                items[i].normalText = DATA[i] + getString(R.string.yuan);
            } else {
                items[i].boldText = DATA[i] / W + "";
                items[i].normalText = getString(R.string.ten_thousand);
            }
        }
        setCircleGroup(currentGroup);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeGroup: {
                changeGroup();
                break;
            }
        }
    }

    public void changeGroup() {
        currentGroup = (currentGroup + 1) % 2;
        setCircleGroup(currentGroup);
    }

    void setCircleGroup(int group) {
        int index = group * 6;
        for (int i = 0; i < circle.length; i++) {
            Item item = items[i + index];
            circle[i].setBoldText(item.boldText);
            circle[i].setNormalText(item.normalText);
            if (item.isSelected) {
                circle[i].setCircle(CircleItem.CIRCLE_RED);
            } else {
                circle[i].setCircle(CircleItem.CIRCLE_BLUE);
            }
        }
    }

    void setData(Item item){

    }

    public class Item {
        private String normalText;
        private String boldText;
        private boolean isSelected;
    }

    private class OnCircleClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int index = currentGroup * 6;
            for (int i = 0; i < circle.length; i++) {
                items[i + index].isSelected = (circle[i] == v);
            }
            setCircleGroup(currentGroup);
        }
    }

}
