package com.svmuu.common.adapter.box;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.svmuu.R;
import com.svmuu.common.adapter.BaseHolder;

public class BoxHolder extends BaseHolder {
    public ImageView icon;
    public TextView text;
    public TextView time;

    public BoxHolder(View itemView) {
        super(itemView);
    }

    protected void initialize() {

        icon = (ImageView) findViewById(R.id.icon);
        text = (TextView) findViewById(R.id.text);
        time = (TextView) findViewById(R.id.time);
    }
}
