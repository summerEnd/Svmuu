package com.sp.lib.widget.parallax.guide;

import android.animation.ObjectAnimator;
import android.view.View;

public class ViewSprite implements ISprite {

    private View contentView;

    ObjectAnimator animator;

    public ViewSprite(View contentView, ObjectAnimator animator) {
        this.contentView = contentView;
        this.animator = animator;
    }

    @Override
    public void run(float delta) {
        long time = (long) (animator.getDuration() * delta);
        animator.setCurrentPlayTime(time);
    }
}
