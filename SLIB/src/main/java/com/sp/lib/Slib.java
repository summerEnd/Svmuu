package com.sp.lib;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sp.lib.exception.ExceptionHandler;
import com.sp.lib.exception.SlibInitialiseException;
import com.sp.lib.support.util.ContextUtil;
import com.sp.lib.support.cache.CacheManager;
import com.sp.lib.support.util.FileUtil;

import java.io.File;

public class Slib {
    public static boolean DEBUG = false;

    /**
     * @param context 一个全局性的Context
     * @param debug   传BuildConfig.DEBUG
     */
    public static final void initialize(Context context, boolean debug) {

        Slib.DEBUG = debug;
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

    private static void initImageLoader(Context context) {

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
    public static final void clearCache() {

        CacheManager.clearAll();
        ImageLoader.getInstance().clearDiskCache();
    }

    /**
     * 注意：Slib的缓存是放在Context.getCacheDir()目录下的。
     * 如果计算整个应用的缓存，要用{@link com.sp.lib.support.util.FileUtil#getSize(java.io.File)} 传入context.getCacheDir();
     *
     * @return Slib缓存的大小
     */
    public static final long getCacheSize() {

        return FileUtil.getSize(ContextUtil.getContext().getCacheDir()) + CacheManager.getCacheSize();
    }

}
