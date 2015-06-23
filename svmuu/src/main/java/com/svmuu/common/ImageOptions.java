package com.svmuu.common;

import android.graphics.Color;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.sp.lib.common.image.RoundDisplayer;
import com.sp.lib.common.image.drawable.RoundDrawable;

/**
 * Created by Lincoln on 15/6/19.
 */
public class ImageOptions {
    public static DisplayImageOptions getStandard(){
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }
    public static DisplayImageOptions getRound(int radius){
        RoundDrawable drawable=new RoundDrawable(Color.GRAY,radius);
        return new DisplayImageOptions.Builder()
                .displayer(new RoundDisplayer(radius))
                .showImageOnFail(drawable)
                .showImageForEmptyUri(drawable)
                .showImageOnLoading(drawable)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }
    public static DisplayImageOptions getRoundCorner(int cornerPixels){
        return new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .displayer(new RoundedBitmapDisplayer(cornerPixels))
                .build();
    }
}
