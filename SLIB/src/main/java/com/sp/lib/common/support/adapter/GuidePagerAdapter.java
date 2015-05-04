package com.sp.lib.common.support.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GuidePagerAdapter extends PagerAdapter {

    private int[] resIds;

    private Activity activity;

    public GuidePagerAdapter(Activity activity, int[] resIds) {
        this.resIds = resIds;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return resIds.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        int resId = resIds[position];
        View v;

        try {
            String type = activity.getResources().getResourceTypeName(resId);
            if ("layout".equals(type)) {
                v = activity.getLayoutInflater().inflate(resId, null);
            } else if ("drawable".equals(type) || "color".equals(type)) {
                ImageView imageView = new ImageView(activity);
                imageView.setImageResource(resId);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                v = imageView;
            } else if ("string".equals(type)) {
                TextView tv = new TextView(activity);
                tv.setGravity(Gravity.CENTER);
                tv.setText(resId);
                v = tv;
            } else {
                throw new RuntimeException("unSupport type:"+type);
            }
        } catch (Resources.NotFoundException e) {
            ImageView imageView = new ImageView(activity);
            imageView.setBackgroundColor(resId);
            v = imageView;
        }

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
