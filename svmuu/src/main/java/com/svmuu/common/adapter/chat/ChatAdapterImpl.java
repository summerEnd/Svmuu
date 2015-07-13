package com.svmuu.common.adapter.chat;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.adapter.BaseAdapter;
import com.svmuu.common.adapter.chat.holders.ChatHolderImpl;
import com.svmuu.common.entity.Chat;

import static com.svmuu.common.adapter.chat.holders.ChatHolderImpl.*;

import java.io.File;
import java.util.List;

public class ChatAdapterImpl extends BaseAdapter<Chat, ChatHolderImpl> {

    DisplayImageOptions options;
    private ChatImageGetter imageGetter;

    public ChatAdapterImpl(Context context, List<Chat> data) {
        super(context, data);
        options = ImageOptions.getRoundCorner(4);
        imageGetter = new ChatImageGetter(context) {
            @Override
            public void onNewBitmapLoaded(Bitmap bitmap) {
                notifyDataSetChanged();
            }
        };
    }



    @Override
    public ChatHolderImpl onCreateViewHolder(ViewGroup parent, int viewType) {
        ChatHolderImpl chatHolder = ChatHolderImpl.newInstance(getContext(), parent, viewType);
        return chatHolder;
    }

    @Override
    public void onBindViewHolder(ChatHolderImpl holder, int position) {
        Chat chat = getData().get(position);
        holder.displayWith(chat, options);
        holder.displayContent(chat, imageGetter);
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = getData().get(position);
        //消息类型 1 普通 2 解盘 3 问答 4 悄悄话 5 公告',

        if (chat.isOwner()) {
            if ("4".equals(chat.type)) {
                return TYPE_WHISPER;
            }
            return TYPE_MASTER;
        }
        int type;
        switch (chat.type) {
            case "1": {
                return TYPE_NORMAL;
            }
            case "2": {
                return TYPE_JP;
            }
            case "3": {
                return TYPE_QA;
            }
            case "4": {
                return TYPE_WHISPER;
            }
            case "5": {
                return TYPE_NOTICE;
            }

        }

        return super.getItemViewType(position);
    }


}
