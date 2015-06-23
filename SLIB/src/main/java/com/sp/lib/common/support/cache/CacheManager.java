package com.sp.lib.common.support.cache;


import android.content.Context;

import com.sp.lib.common.exception.SlibInitialiseException;

import java.io.File;
import java.util.List;

/**
 * 封装一个{@link ICache}，实现缓存的单例模式
 */
public class CacheManager {
    private static File CACHE_DIR;

    private ICache mCache;
    /**
     * 存放错误报告的缓存
     */
    public static final String ERROR_LOGS = "slib_error_logs";

    /**
     * 隐藏构造方法
     */
    private CacheManager(ICache cache) {
        this.mCache = cache;
    }

    public static CacheManager getInstance() {
        return new CacheManager(new FileObjectCache(CACHE_DIR));
    }

    public static CacheManager getBitmapInstance() {
        return new CacheManager(new FileBitmapCache(CACHE_DIR));
    }

    public static void init(Context context) {
        CACHE_DIR = context.getCacheDir();
    }

    public List listKeys() {
        return getCache().listKeys();
    }

    public Object read(Object key) {
        return getCache().read(key);
    }

    public Object write(Object key, Object data) {
        return getCache().write(key, data);
    }

    public void clear() {
        getCache().clear();
    }

    public Object remove(Object key) {
        return getCache().remove(key);
    }

    public List listObjects() {
        return getCache().listObjects();
    }

    private ICache getCache() {
        if (mCache == null) {
            throw new SlibInitialiseException(CacheManager.class);
        }
        return mCache;
    }

    public static void clearAll() {
        getInstance().clear();
        getBitmapInstance().clear();
    }

    /**
     * 获取CacheManager下管理的缓存的大小
     *
     * @return
     */
    public static long getCacheSize() {
        return getBitmapInstance().getCache().size() + getInstance().getCache().size();
    }

}
