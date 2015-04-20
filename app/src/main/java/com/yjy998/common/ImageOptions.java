package com.yjy998.common;

import android.graphics.Color;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.sp.lib.common.drawable.RoundDrawable;
import com.sp.lib.common.image.RoundDisplayer;

public class ImageOptions {
    public static DisplayImageOptions getAvatarInstance(int radius) {

        return new DisplayImageOptions.Builder()
                .displayer(new RoundDisplayer(radius))
                .showImageOnFail(new RoundDrawable(Color.WHITE, radius))
                .showImageForEmptyUri(new RoundDrawable(Color.WHITE, radius))
                .showImageOnLoading(new RoundDrawable(Color.WHITE, radius))
                .build();
    }
}
