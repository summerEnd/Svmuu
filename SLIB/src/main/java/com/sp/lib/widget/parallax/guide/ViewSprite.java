package com.sp.lib.widget.parallax.guide;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

public abstract class ViewSprite implements ISprite {

    protected int DURATION = 1000;
    protected View contentView;
    protected ObjectAnimator[] animators;
    private boolean repeat=false;
    public ViewSprite(View contentView) {
        this.contentView = contentView;
        animators = createAnimators(contentView);
    }

    @Override
    public void run(float delta) {

        for (Animator anim : animators) {
            long time = (long) (DURATION * delta);

            ObjectAnimator animator = (ObjectAnimator) anim;
            long duration = animator.getDuration();
            if (duration >= time){
                animator.setCurrentPlayTime(time);
            }else if (repeat){
                animator.setCurrentPlayTime(time%duration);
            }
        }
    }

    public abstract ObjectAnimator[] createAnimators(View v);

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }
}
