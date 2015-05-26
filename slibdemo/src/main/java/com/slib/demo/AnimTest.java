package com.slib.demo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sp.lib.ToggleView;
import com.sp.lib.common.support.adapter.GuidePagerAdapter;
import com.sp.lib.common.util.DisplayUtil;
import com.sp.lib.widget.parallax.guide.ViewSprite;
import com.svmuu.slibdemo.R;

import java.util.ArrayList;
import java.util.Random;

public class AnimTest extends SLIBTest implements ViewPager.OnPageChangeListener {
    ViewGroup infoLayout;
    ViewGroup cloudLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);
        ViewGroup above = (ViewGroup) getLayoutInflater().inflate(R.layout.item_layout, null);

        ViewPager pager = new ViewPager(this);
        setContentView(frameLayout);
        frameLayout.addView(pager);
        frameLayout.addView(above);
        initPager(pager);
        intAbove(above);

    }

    private void initPager(ViewPager pager) {
        int[] colors = new int[8];
        int red;
        int green;
        int blue;
        Random random = new Random(7715);
        for (int i = 0; i < colors.length; i++) {
            red = random.nextInt(256);
            green = random.nextInt(256);
            blue = random.nextInt(256);
            colors[i] = Color.rgb(red, green, blue);
        }

        pager.setAdapter(new GuidePagerAdapter(this, colors));
        pager.setOnPageChangeListener(this);
    }

    ArrayList<ViewSprite> sprites = new ArrayList<ViewSprite>();
    ToggleView toggleView;
    private void intAbove(ViewGroup above) {
        infoLayout = (ViewGroup) above.findViewById(R.id.infoLayout);
        cloudLayout = (ViewGroup) above.findViewById(R.id.cloudLayout);
        int count = infoLayout.getChildCount();
        for (int j = 0; j < count; j++) {
            View childAt = infoLayout.getChildAt(j);
            InfoSprite object = new InfoSprite(childAt);
            object.setRepeat(true);
            sprites.add(object);
        }

        for (int i = 1; i < cloudLayout.getChildCount(); i++) {
            View childAt = cloudLayout.getChildAt(i);
            sprites.add(new CloudSprite(childAt));
        }

        toggleView= (ToggleView) above.findViewById(R.id.toggle);

    }

    private class CloudSprite extends ViewSprite {

        public CloudSprite(View contentView) {
            super(contentView);
        }

        @Override
        public ObjectAnimator[] createAnimators(View v) {

            ObjectAnimator cloud[] = new ObjectAnimator[1];
            int duration = (new Random().nextInt(5) + 1) * this.DURATION;
            int screenWidth = DisplayUtil.getScreenWidth(v.getContext());
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("Alpha", 0, 1, 0);
            PropertyValuesHolder translation = PropertyValuesHolder.ofFloat("TranslationX", screenWidth, 0);
            cloud[0] = ObjectAnimator.ofPropertyValuesHolder(v, alpha, translation);
            cloud[0].setDuration(duration);

            return cloud;
        }
    }

    private class InfoSprite extends ViewSprite {

        public InfoSprite(View contentView) {
            super(contentView);
        }

        @Override
        public ObjectAnimator[] createAnimators(View v) {

            ObjectAnimator animator[] = new ObjectAnimator[1];
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("Alpha", 0, 0.9f, 1, 0.9f, 0);
            PropertyValuesHolder translation = PropertyValuesHolder.ofFloat("TranslationY", 0,500, 500, 500, 0);
            animator[0] = ObjectAnimator.ofPropertyValuesHolder(v, alpha, translation);
            animator[0].setDuration(DURATION * 2);
            return animator;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
        for (ViewSprite sprite : sprites) {
            sprite.run(i + v);
        }
        toggleView.setRatio(v);
    }

    @Override
    public void onPageSelected(int i) {
        int count = infoLayout.getChildCount();
        for (int j = 0; j < count; j++) {
            ((TextView) infoLayout.getChildAt(j)).setText("HELLO" + i);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
