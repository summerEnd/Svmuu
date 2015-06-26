package com.svmuu.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.svmuu.common.ImageOptions;

public class AvatarView extends ImageView {
    String url;
    ImageOptions options;

    public AvatarView(Context context) {
        this(context, null);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void show(String url) {

    }
}
