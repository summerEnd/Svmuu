package com.svmuu.common.adapter.master;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.svmuu.R;
import com.svmuu.common.adapter.BaseHolder;
import com.svmuu.common.entity.CircleMaster;
import com.svmuu.ui.activity.live.LiveActivity;

/**
 * Created by Lincoln on 15/6/19.
 */
public class MasterAvatarHolder extends BaseHolder{
    public ImageView ivavatar;
    public TextView tvname;
    public MasterAvatarHolder(View itemView) {
        super(itemView);

    }
    protected void initialize() {

        ivavatar = (ImageView) findViewById(R.id.iv_avatar);
        tvname = (TextView) findViewById(R.id.tv_name);
    }


}
