package com.sp.lib;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sp.lib.demo.SlibDemo;
import com.sp.lib.exception.ExceptionHandler;
import com.sp.lib.exception.SlibInitialiseException;
import com.sp.lib.support.cache.CacheManager;
import com.sp.lib.support.util.ContextUtil;
import com.sp.lib.support.util.FileUtil;

import java.io.File;
import java.util.List;

public class SApplication extends Application {

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
     * 如果计算整个应用的缓存，要用{@link com.sp.lib.support.util.FileUtil#getSize(java.io.File)} 传入context.getCacheDir();
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

}
