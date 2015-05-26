package com.sp.lib.widget.pager;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sp.lib.R;
import com.sp.lib.common.util.DisplayUtil;
import com.sp.lib.widget.slide.transform.Transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BannerPager extends FrameLayout implements RadioGroup.OnCheckedChangeListener {
    private ViewPager pager;
    private RadioGroup dotRadioGroup;
    private List<String> imageUrls;
    private int dotDrawable = R.drawable.indicator_dot;
    private BannerClickListener mBannerClickListener;
    private Timer timer;
    private Transformer transformer;

    public BannerPager(Context context) {
        this(context, null);
    }

    public BannerPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        pager = new ViewPager(getContext());
        dotRadioGroup = new RadioGroup(getContext());
        dotRadioGroup.setOrientation(LinearLayout.HORIZONTAL);

        addView(pager);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        params.bottomMargin = 20;
        addView(dotRadioGroup, params);
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setTransformer(ViewPager.PageTransformer transformer) {
        pager.setPageTransformer(true, transformer);
    }


    public void setDotDrawable(int dotDrawable) {
        this.dotDrawable = dotDrawable;
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int curItem = pager.getCurrentItem();
            PagerAdapter adapter = pager.getAdapter();

            if (adapter == null) {
                //banner还有没加载出来
                return false;
            }

            //如果是最后一个,就跳转到第一个
            if (curItem == adapter.getCount() - 1) {
                dotRadioGroup.check(0);
            } else {
                dotRadioGroup.check(curItem + 1);
            }
            return false;
        }
    });

    /**
     * 初始化广告条
     */
    void initBanner() {

        if (imageUrls == null || imageUrls.size() == 0) {
            return;
        }

        dotRadioGroup.setOnCheckedChangeListener(this);
        pager.setOnPageChangeListener(new BannerPagerListener());
        ArrayList<View> views = new ArrayList<View>();
        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().cacheOnDisk(true).build();
        for (int i = 0; i < imageUrls.size(); i++) {
            //添加RadioButton
            Context context = getContext();
            RadioButton rb = new RadioButton(context);
            if (dotDrawable != 0) {
                rb.setBackgroundResource(dotDrawable);
            }
            rb.setButtonDrawable(new ColorDrawable(0));
            rb.setId(i);

            //设置RadioButton大小
            int margins = (int) DisplayUtil.dp(3, getResources());
            int width;
            int height = width = (int) DisplayUtil.dp(8, getResources());
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(width, height);
            lp.setMargins(margins, margins, margins, margins);
            dotRadioGroup.addView(rb, lp);

            //添加图片
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setOnClickListener(mBannerClickListener);
            ImageLoader.getInstance().displayImage(imageUrls.get(i), imageView,imageOptions);
            views.add(imageView);
        }
        dotRadioGroup.check(0);
        pager.setAdapter(new BannerAdapter(views));
    }

    public void start(int duration) {

        initBanner();
        if (timer != null) {
            try {
                timer.cancel();
            } catch (Exception ignored) {
            }
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(1);
            }
        }, 0, duration);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        pager.setCurrentItem(checkedId);
    }

    public void setBannerClickListener(BannerClickListener bannerClickListener) {
        this.mBannerClickListener = bannerClickListener;
    }

    private abstract class BannerClickListener implements OnClickListener {

        @Override
        public final void onClick(View v) {
            int position;
            try {
                position = (Integer) v.getTag(R.id.slib_position);
            } catch (Exception e) {
                position = 0;
            }
            onBannerClick(position);
        }

        public abstract void onBannerClick(int position);
    }

    private class BannerPagerListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            dotRadioGroup.check(position);
        }
    }


    //广告条滚动
    class BannerAdapter extends PagerAdapter {
        private List<View> views;

        BannerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View v = views.get(position);
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = views.get(position);
            container.removeView(v);
        }
    }
}
