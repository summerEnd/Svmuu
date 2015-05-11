package com.sp.lib.common.support.cache;

import com.sp.lib.common.exception.SlibInitialiseException;
import com.sp.lib.common.util.FileUtil;
import com.sp.lib.common.admin.Md5;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 基于文件实现的缓存
 */
public abstract class FileCache<D> implements ICache<String, D> {
    private File cacheDir;

    /**
     * @param dir 存放缓存的目录。程序将在该目录下再创建一个目录，放置本框架的缓存。
     */
    public FileCache(File dir) {
        if (dir == null) {
            throw new SlibInitialiseException(getClass().getName() + "必须要有一个缓存目录！");
        } else {

            if (!dir.isDirectory()) {
                throw new SlibInitialiseException(dir + "不是一个目录！");
            }
            cacheDir = new File(dir, Md5.md5(getClass().getSimpleName()));
            if (!cacheDir.isDirectory()) {
                cacheDir.mkdir();
            }
        }
    }

    public File getCacheDir() {
        return cacheDir;
    }

    @Override
    public final D remove(String key) {
        File f = null;
        try {
            f = getFile(key);
        } catch (IOException e) {
            return null;
        }
        D data = read(key);
        f.delete();
        return data;
    }

    @Override
    public final void clear() {
        FileUtil.delete(getCacheDir());
    }

    @Override
    public List<D> listObjects() {

        List<D> objects = new LinkedList<D>();
        for (String key : listKeys()) {
            objects.add(read(key));
        }
        return objects;
    }

    @Override
    public final List<String> listKeys() {
        List<String> keys = new LinkedList<String>();
        for (File f : getCacheDir().listFiles()) {
            keys.add(f.getName());
        }
        return keys;
    }

    @Override
    public long size() {

        return FileUtil.getSize(getCacheDir());
    }

    protected File getFile(String key) throws IOException {
        return new File(getCacheDir(), key);
    }
}
