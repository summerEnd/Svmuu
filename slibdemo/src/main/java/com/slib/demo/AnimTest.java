package com.slib.demo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nineoldandroids.util.FloatProperty;
import com.sp.lib.common.support.adapter.GuidePagerAdapter;
import com.sp.lib.widget.parallax.guide.ViewSprite;
import com.svmuu.slibdemo.R;

import java.util.ArrayList;
import java.util.Random;

public class AnimTest extends SLIBTest implements ViewPager.OnPageChangeListener {
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

    private void intAbove(ViewGroup above) {
        for (int i = 0; i < above.getChildCount(); i++) {
            View childAt = above.getChildAt(i);
            ViewSprite sprite = new ViewSprite(childAt, animator(childAt));
            sprites.add(sprite);
        }
    }

    private ObjectAnimator animator(View v) {
        AnimatorSet set=new AnimatorSet();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(v, "TranslationY", 0, 500, 100, 1000,0);
        translationY.setDuration(5000);
        set.playTogether(translationY);
        return null;
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
        for (ViewSprite sprite : sprites) {
            sprite.run(v);
        }
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
