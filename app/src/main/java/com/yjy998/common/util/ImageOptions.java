package com.yjy998.common.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.sp.lib.common.image.drawable.RoundDrawable;
import com.sp.lib.common.image.RoundDisplayer;
import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;

public class ImageOptions {
    public static DisplayImageOptions getAvatarInstance(int radius) {
        return new DisplayImageOptions.Builder()
                .displayer(new RoundDisplayer(radius))
                .showImageOnFail(R.drawable.avatar)
                .showImageForEmptyUri(R.drawable.avatar)
                .showImageOnLoading(R.drawable.avatar)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    public static DisplayImageOptions getAvatarInstance() {
        return getAvatarInstance(ContextUtil.getContext().getResources().getDimensionPixelSize(R.dimen.avatarSize));
    }
}
