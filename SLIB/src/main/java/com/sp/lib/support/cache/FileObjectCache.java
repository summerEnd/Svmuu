package com.sp.lib.support.cache;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 基于{@link java.io.ObjectOutputStream}实现缓存，写入和读取的对象要实现{@link java.io.Serializable}接口
 */
public class FileObjectCache extends FileCache<Object> {
    private String TAG = getClass().getSimpleName();


    public FileObjectCache(File dir) {
        super(dir);
    }

    @Override
    public Object read(String key) {
        Object obj = null;
        try {
            FileInputStream is = new FileInputStream(getFile(key));
            ObjectInputStream ois = new ObjectInputStream(is);
            obj = ois.readObject();
        } catch (Exception e) {
            Log.w(TAG, e.getMessage());
        }
        return obj;
    }


    @Override
    public Object write(String key, Object o) {
        try {
            FileOutputStream os = new FileOutputStream(getFile(key));
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(o);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return o;
    }



}
