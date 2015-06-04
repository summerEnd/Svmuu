package com.yjy998.ui.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import com.sp.lib.widget.pager.title.TextPageTab;
import com.yjy998.R;

public class ViewPagerTabItem extends TextPageTab {
    ObjectAnimator animation;

    public ViewPagerTabItem(Context context) {
        this(context, null);
    }

    public ViewPagerTabItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerTabItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onTabSelected(boolean selected) {
        if (animation == null) {
            animation = createAnim();
        } else {
            animation.setCurrentPlayTime(0);
        }

        if (selected) {
            setTextColor(getResources().getColor(R.color.deepBlue));
            animation.start();
        } else {
            setTextColor(getResources().getColor(R.color.textColorDeepGray));
        }
    }

    ObjectAnimator createAnim() {


        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this,
                PropertyValuesHolder.ofFloat("ScaleX", 1, 1.2f),
                PropertyValuesHolder.ofFloat("ScaleY", 1, 1.2f)
        );
        animator.setDuration(200);
        animator.setInterpolator(new AccelerateInterpolator());
        return animator;
    }
}
