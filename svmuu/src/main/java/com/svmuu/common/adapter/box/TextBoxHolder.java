package com.svmuu.common.adapter.box;

import android.view.View;
import android.widget.TextView;

import com.svmuu.R;
import com.svmuu.common.adapter.BaseHolder;


public class TextBoxHolder extends BaseHolder<Object> {
    public TextView timeText;
    public TextView contentText;

    public TextBoxHolder(View itemView) {
        super(itemView);
    }

    protected void initialize() {

        timeText = (TextView) findViewById(R.id.timeText);
        contentText = (TextView) findViewById(R.id.contentText);
    }
}
