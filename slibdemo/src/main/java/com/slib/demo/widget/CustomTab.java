package com.slib.demo.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import com.sp.lib.widget.nav.title.TextPageTab;


public class CustomTab extends TextPageTab {
    private Animation mAnim;

    public CustomTab(Context context) {
        this(context, null);
    }

    public CustomTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTab(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

    }

    @Override
    public void onTabSelected(boolean selected) {

        clearAnimation();
        if (selected) {
            setTextColor(Color.RED);
           if (mAnim ==null){
                mAnim=createAnim();
            }
            startAnimation(mAnim);
        } else {
            setTextColor(Color.GRAY);
        }
    }

    Animation createAnim() {
        AnimationSet set = new AnimationSet(true);
        set.setDuration(400);
        set.addAnimation(new ScaleAnimation(1, 1.1f, 1, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
        set.setFillAfter(true);
        return set;
    }
}