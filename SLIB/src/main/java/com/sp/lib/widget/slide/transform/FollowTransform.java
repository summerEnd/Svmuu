package com.sp.lib.widget.slide.transform;

import android.view.View;

public class FollowTransform implements Transformer {

    float followWidth;

    public FollowTransform(float followWidth) {
        this.followWidth = followWidth;
    }

    @Override
    public void transform(View main, View menu, float delta) {
        menu.setTranslationX(followWidth * delta - followWidth);
        menu.setAlpha(Math.max(0.5f, delta));
    }
}
