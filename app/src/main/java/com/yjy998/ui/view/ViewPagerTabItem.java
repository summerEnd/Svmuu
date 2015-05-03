package com.yjy998.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import com.sp.lib.widget.pager.title.TextPageTab;
import com.yjy998.R;

public class ViewPagerTabItem extends TextPageTab {
    Animation animation;

    public ViewPagerTabItem(Context context) {
        this(context, null);
    }

    public ViewPagerTabItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerTabItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        animation = createAnim();
    }

    @Override
    protected void onTabSelected(boolean selected) {
        clearAnimation();
        if (selected) {
            setTextColor(getResources().getColor(R.color.deepBlue));
            startAnimation(animation);
        } else {
            setTextColor(getResources().getColor(R.color.textColorDeepGray));
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
