package com.svmuu.common.adapter.live;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.sp.lib.common.util.DisplayUtil;
import com.svmuu.R;
import com.svmuu.common.adapter.BaseHolder;
import com.svmuu.ui.activity.live.LiveActivity;


public class LiveHolder extends BaseHolder {
    public ImageView ivcover;
    public TextView tvfansNumber;
    public TextView tvcircleName;
    public TextView tvcircleNo;
    public View live;

    public LiveHolder(@NonNull View itemView) {
        super(itemView);

    }
    protected void initialize() {
        live=findViewById(R.id.live);
        ivcover = (ImageView) findViewById(R.id.iv_cover);
        tvfansNumber = (TextView) findViewById(R.id.tv_fansNumber);
        tvcircleName = (TextView) findViewById(R.id.tv_circleName);
        tvcircleNo = (TextView) findViewById(R.id.tv_circleNo);
        LayoutParams params=findViewById(R.id.imageContainer).getLayoutParams();
        if (params!=null){
            //变成正方形
            int screenWidth = DisplayUtil.getScreenWidth(ivcover.getContext());
            int size= screenWidth*7/27;
            int padding=screenWidth/27;
            params.height=size;
            params.width=size;
            ivcover.getLayoutParams().width=size;
            ivcover.getLayoutParams().height=size;
            itemView.setPadding(0,padding,0,padding);
        }
    }

    @Override
    public void onClick(View v) {
        OnItemListener listener = getListener();
        if (listener!=null){
            listener.onClick(v,getAdapterPosition());
        }
    }
}
