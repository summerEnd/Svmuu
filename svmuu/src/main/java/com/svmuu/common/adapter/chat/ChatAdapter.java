package com.svmuu.common.adapter.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.svmuu.R;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.Tests;
import com.svmuu.common.adapter.BaseAdapter;
import com.svmuu.common.entity.Chat;
import com.svmuu.ui.widget.ChatItemView;

import java.util.List;

public class ChatAdapter extends BaseAdapter<Chat, ChatHolder> {
    DisplayImageOptions options;
    private Html.ImageGetter imageGetter;

    public ChatAdapter(@NonNull final Context context, List<Chat> data) {
        super(context, data);
        options = ImageOptions.getRoundCorner(4);
        imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                final BitmapDrawable d = new BitmapDrawable(context.getResources());
                ImageLoader.getInstance().loadImage(source, ImageOptions.getStandard(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
                return d;
            }
        };
    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatHolder(new ChatItemView(getContext(), viewType == 0));
    }

    @Override
    public int getItemViewType(int position) {
        return "1".equals(getData().get(position).is_owner) ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        Chat chat = getData().get(position);
        holder.setData(chat);

        holder.getContentTextView().setText(Html.fromHtml(chat.content));
        ImageLoader.getInstance().displayImage(chat.uface, holder.getAvatarView(), options);
    }

}
