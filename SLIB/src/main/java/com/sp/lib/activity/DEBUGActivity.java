package com.sp.lib.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sp.lib.R;
import com.sp.lib.Slib;
import com.sp.lib.support.cache.CacheManager;

import java.util.LinkedList;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static com.sp.lib.exception.ExceptionHandler.ErrorLog;

/**
 * 这个类可以做以下事情
 * 1.显示过去程序中未捕获的异常
 * 2.查看当前应用信息
 */
public class DEBUGActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {
    LinkedList<ErrorLog> logList;
    LinkedList<View> views;
    ViewPager viewPager;
    String app_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app_name = getTitle().toString();

        logList = (LinkedList<ErrorLog>) CacheManager.getInstance().read(CacheManager.ERROR_LOGS);
        viewPager = new ViewPager(this);
        setContentView(viewPager);

        if (logList != null) {
            views = new LinkedList<View>();
            viewPager.setPageTransformer(true, this);
            viewPager.setOnPageChangeListener(this);
            viewPager.setAdapter(new MyAdapter());
            showTitle(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.debug_menu, menu);
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            menu.findItem(R.id.slib_orientation).setTitle(R.string.portrait);
        } else {
            menu.findItem(R.id.slib_orientation).setTitle(R.string.landscape);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.slib_orientation) {
            //横竖屏切换
            toggleScreenOrientation(item);
            return true;
        } else if (itemId == R.id.slib_application) {
            PackageInfo info = null;
            try {
                info = getPackageManager().getPackageInfo(getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            StringBuilder message = new StringBuilder();

            message
                    .append("包名：").append(info.packageName).append("\n")
                    .append("应用名：").append(getString(info.applicationInfo.labelRes)).append("\n")
                    .append("版本：").append(info.versionName).append("\n")
                    .append("版本号：").append(info.versionCode).append("\n")
                    .append("DEBUG：").append(Slib.DEBUG).append("\n")
            ;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(item.getTitle())
                    .setMessage(message)
                    .setPositiveButton("确定", null)
                    .show();
            return true;
        }


        if (logList == null || logList.isEmpty()) {
            viewPager.setVisibility(View.INVISIBLE);
            return false;
        }


        //当前viewPager
        int curPageIndex = viewPager.getCurrentItem();

        if (itemId == R.id.slib_share) {
            //发送日志
            ErrorLog errorLog = logList.get(curPageIndex);
            String content = errorLog.time + "\n" + errorLog.msg;
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, content);
            startActivity(Intent.createChooser(intent, "SEND"));

        } else if (itemId == R.id.slib_delete) {
            //删除当前页
            logList.remove(curPageIndex);
            showTitle(--curPageIndex);
            viewPager.setAdapter(new MyAdapter());
            viewPager.setCurrentItem(curPageIndex);

        } else if (itemId == R.id.slib_next) {
            //下一条
            viewPager.setCurrentItem(isLastPage() ? 0 : curPageIndex + 1);

        } else if (itemId == R.id.slib_previous) {
            //上一条
            viewPager.setCurrentItem(isFirstPage() ? logList.size() - 1 : curPageIndex - 1);

        } else if (itemId == R.id.slib_clear) {
            //清空日志
            logList.clear();
            viewPager.getAdapter().notifyDataSetChanged();
            viewPager.setVisibility(View.INVISIBLE);
            showTitle(0);
        }

        return true;
    }


    boolean isLastPage() {
        return viewPager.getCurrentItem() == logList.size() - 1;
    }

    boolean isFirstPage() {
        return viewPager.getCurrentItem() == 0;
    }

    private void toggleScreenOrientation(MenuItem item) {
        if (getRequestedOrientation() == SCREEN_ORIENTATION_LANDSCAPE) {
            item.setTitle(R.string.portrait);
            setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        } else {
            item.setTitle(R.string.landscape);
            setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        CacheManager.getInstance().write(CacheManager.ERROR_LOGS, logList);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
        showTitle(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    void showTitle(int index) {
        int size = logList.size();

        if (size == 0) {
            setTitle(app_name);
            return;
        }
        index = Math.max(0, index);
        if (size < index) {
            index = size;
        }

        setTitle(String.format("%s %d/%d", app_name, index + 1, size));
    }

    @Override
    public void transformPage(View view, float position) {
        float pageWidth = view.getWidth();
        float MIN_SCALE = 0.2f;
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 1) { // [-1,1]

            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float factor = Math.abs(position);

            if (position > 0) {
                view.setTranslationX(-pageWidth * factor / 3);
                view.setRotationY(-(1 - scaleFactor) * 90);

            } else {
                view.setTranslationX(pageWidth * factor / 3);
                view.setRotationY((1 - scaleFactor) * 90);
            }
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            view.setAlpha(1 - factor);
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }


    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return logList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view.equals(o);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v;
            ViewHolder holder;
            if (!views.isEmpty()) {
                v = views.pop();
                holder = (ViewHolder) v.getTag();
            } else {
                holder = new ViewHolder();
                v = getLayoutInflater().inflate(R.layout.erro_log, null);
                holder.log = (TextView) v.findViewById(R.id.tv_logs);
                holder.time = (TextView) v.findViewById(R.id.tv_time);
                v.setTag(holder);
            }
            ErrorLog error = logList.get(position);
            holder.log.setText(error.msg);
            holder.time.setText(error.time);
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
            views.push(v);
        }
    }

    class ViewHolder {
        TextView log;
        TextView time;

    }
}
