package com.svmuu.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.gensee.chat.gif.GifDrawalbe;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sp.lib.common.util.ImageUtil;
import com.svmuu.ui.widget.GifDrawable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by user1 on 2015/7/15.
 * 用来展示头像的工具类
 */
public class AvatarUtil {
    public static void display(final String avatar, final ImageView avatarImage) {


        if (!TextUtils.isEmpty(avatar)) {
            File avatarFile = ImageLoader.getInstance().getDiskCache().get(avatar);
            if (avatarFile != null && avatarFile.exists()) {
                if (avatar.endsWith(".gif")) {
                    displayGif(avatarImage, avatarFile);
                } else {
                    displayNormal(avatar, avatarImage);
                }
            } else {
                //下载图片
                download(avatar, avatarImage);
            }
        }
    }

    /**
     * 下载头像
     *
     * @param avatar      头像链接
     * @param avatarImage 用来展示头像
     */
    private static void download(final String avatar, final ImageView avatarImage) {
        ImageLoader.getInstance().displayImage(avatar, avatarImage, ImageOptions.getRound(avatarImage.getMeasuredWidth() / 2), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                //下载成功继续展示
                display(avatar, avatarImage);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

    }

    /**
     * 展示普通图片
     *
     * @param avatar      头像链接
     * @param avatarImage 用来展示头像
     */
    private static void displayNormal(String avatar, ImageView avatarImage) {
        ImageLoader.getInstance().displayImage(avatar, avatarImage, ImageOptions.getRound(avatarImage.getMeasuredWidth() / 2));//预加载一遍
    }

    /**
     * 展示gif图
     *
     * @param avatarImage 头像链接
     * @param avatarFile  用来展示头像
     */
    private static void displayGif(final ImageView avatarImage, File avatarFile) {
        try {
            GifDrawable gifDrawable = new GifDrawable(avatarImage.getContext(), new FileInputStream(avatarFile));
            gifDrawable.addListen(new GifDrawable.UpdateUIListen() {
                @Override
                public void updateUI() {
                    avatarImage.invalidate();
                }
            });
            gifDrawable.readFrames(false);
            avatarImage.setImageDrawable(gifDrawable);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
