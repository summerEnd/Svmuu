package com.sp.lib;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sp.lib.demo.SlibDemo;
import com.sp.lib.common.exception.ExceptionHandler;
import com.sp.lib.common.exception.SlibInitialiseException;
import com.sp.lib.common.support.cache.CacheManager;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.FileUtil;
import com.sp.lib.common.util.PreferenceUtil;

import java.io.File;
import java.util.List;

public class SApplication extends Application {
    //锁屏的时间间隔
    private final int BACK_DURATION = 1 * 60 * 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        try {
            initImageLoader(context);
            ExceptionHandler.init();
            ContextUtil.init(context);
            //初始化缓存框架
            CacheManager.init(context);
        } catch (SlibInitialiseException e) {
            e.printStackTrace();
        }
    }

    public void setMainTest(Class<? extends Activity> activity) {
        SlibDemo.setMainTest(activity);
    }

    public void setDebug(boolean debug) {
        SApplication.DEBUG = debug;
    }

    public static boolean DEBUG = false;

    private void initImageLoader(Context context) {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(1 * 1024 * 1024))
                .diskCacheSize(50 * 1024 * 1024)
                .threadPoolSize(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCache(new UnlimitedDiscCache(new File(context.getCacheDir(), "image_loader")))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * 清除Slib的所有缓存
     */
    public final void clearCache() {
        CacheManager.clearAll();
        ImageLoader.getInstance().clearDiskCache();
    }

    /**
     * 注意：Slib的缓存是放在Context.getCacheDir()目录下的。
     * 如果计算整个应用的缓存，要用{@link com.sp.lib.common.util.FileUtil#getSize(java.io.File)} 传入context.getCacheDir();
     *
     * @return Slib缓存的大小
     */
    public final long getCacheSize() {

        return FileUtil.getSize(ContextUtil.getContext().getCacheDir()) + CacheManager.getCacheSize();
    }

    /**
     * app是否在前台运行
     *
     * @return
     */
    public boolean didAppRunForeground() {
        int importance = getProcessImportance(this);
        return importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
    }

    public static int getProcessImportance(Context applicationContext) {
        ActivityManager manager = (ActivityManager) applicationContext.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = applicationContext.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();
        if (runningAppProcesses != null)
            for (ActivityManager.RunningAppProcessInfo app : runningAppProcesses) {
                if (app.processName.equals(packageName)) {
                    return app.importance;
                }
            }
        return ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY;
    }

    /**
     * 设置当前app是否进入后台
     */
    public void setEnterBackground(boolean background) {
        PreferenceUtil.getPreference(AppInfo.class).edit()
                .putBoolean(AppInfo.background, background)
                .putLong(AppInfo.enterBackgroundTimeMillis, System.currentTimeMillis())
                .commit();
    }

    /**
     * 是否应锁屏。如果进入后台时间大于{@link com.sp.lib.SApplication#BACK_DURATION 锁屏间隔}就锁屏
     *
     * @return true 锁屏
     */
    public boolean shouldLockScreen() {
        SharedPreferences appInfo = PreferenceUtil.getPreference(AppInfo.class);
        boolean background = appInfo.getBoolean(AppInfo.background, false);

        if (background) {
            long enterBackgroundTimeMillis = appInfo.getLong(AppInfo.enterBackgroundTimeMillis, 0);
            return System.currentTimeMillis() - enterBackgroundTimeMillis > BACK_DURATION;
        }
        return false;
    }
}
