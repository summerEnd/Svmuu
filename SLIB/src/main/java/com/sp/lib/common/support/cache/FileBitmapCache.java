package com.sp.lib.common.support.cache;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileBitmapCache extends FileCache<Bitmap> {
    public FileBitmapCache(File dir) {
        super(dir);
    }

    @Override
    public Bitmap read(String key) {
        return BitmapFactory.decodeFile(new File(getCacheDir(), key).getAbsolutePath());
    }


    /**
     * 写入图片缓存
     * @param key
     * @param data
     * @return 代表这个图片路径的Uri
     */
    @Override
    public Uri write(String key, Bitmap data) {
        if (TextUtils.isEmpty(key) || data == null) {
            return null;
        }
        File f = new File(getCacheDir(), key);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(f);
            data.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return Uri.fromFile(f);
    }

}
