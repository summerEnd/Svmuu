package com.svmuu.common.adapter.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;

import com.gensee.chat.gif.GifDrawalbe;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.svmuu.R;
import com.svmuu.common.ImageOptions;

import java.io.File;

/**
 * Created by user1 on 2015/7/10.
 * 聊天图片加载工具
 */
public abstract class ChatImageGetter implements Html.ImageGetter, GifDrawalbe.UpdateUIListen {

    private Context context;
    private final int MAX_WIDTH;//图片的最大宽度
    private ColorDrawable empty;
    public ChatImageGetter(Context context) {
        this.context = context;
        MAX_WIDTH = context.getResources().getDimensionPixelOffset(R.dimen.dimen_270px);
        empty=new ColorDrawable();
    }


    @Override
    public Drawable getDrawable(String source) {
        File file = ImageLoader.getInstance().getDiskCache().get(source);
        if (file != null && file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            int width;
            int height;
            if (options.outWidth > MAX_WIDTH) {
                width = MAX_WIDTH;
                height = (int) (options.outHeight * (MAX_WIDTH / (float) options.outWidth));
            } else {
                width = options.outWidth;
                height = options.outHeight;
            }
            Drawable d = new BitmapDrawable(context.getResources(), bitmap);

            d.setBounds(0, 0, width, height);
            return d;
        }
        ImageLoader.getInstance().loadImage(source, ImageOptions.getStandard(), listener);
        return empty;
    }

    @Override
    public void updateUI() {
    }

    private ImageLoadingListener listener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String s, View view) {

        }

        @Override
        public void onLoadingFailed(String s, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            onNewBitmapLoaded(bitmap);
        }

        @Override
        public void onLoadingCancelled(String s, View view) {

        }
    };

    public abstract void onNewBitmapLoaded(Bitmap bitmap);
}
