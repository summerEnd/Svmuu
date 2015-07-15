package com.svmuu.common.adapter.chat;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.adapter.BaseAdapter;
import com.svmuu.common.adapter.chat.holders.ChatHolderImpl;
import com.svmuu.common.entity.Chat;

import static com.svmuu.common.adapter.chat.holders.ChatHolderImpl.*;

import java.util.List;

public class ChatAdapterImpl extends BaseAdapter<Chat, ChatHolderImpl> {

    DisplayImageOptions options;
    private ChatImageGetter imageGetter;
    private ChatTagHandler tagHandler;

    public ChatAdapterImpl(Context context, List<Chat> data) {
        super(context, data);
        options = ImageOptions.getRoundCorner(4);
        imageGetter = new ChatImageGetter(context) {
            @Override
            public void onNewBitmapLoaded(Bitmap bitmap) {
                notifyDataSetChanged();
            }
        };
//        tagHandler=new ChatTagHandler();
    }


    @Override
    public ChatHolderImpl onCreateViewHolder(ViewGroup parent, int viewType) {
        return ChatHolderImpl.newInstance(getContext(), parent, viewType);
    }

    @Override
    public void onBindViewHolder(ChatHolderImpl holder, int position) {
        Chat chat = getData().get(position);
        holder.displayWith(chat, options);
        holder.displayContent(chat, imageGetter, tagHandler);
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = getData().get(position);
        //消息类型 1 普通 2 解盘 3 问答 4 悄悄话 6 公告',

        if (chat.isOwner()) {
            if ("4".equals(chat.type)) {
                return TYPE_WHISPER;
            }

            if ("2".equals(chat.type)) {
                return TYPE_JP;
            }

            if ("3".equals(chat.type)) {
                return TYPE_QA;
            }

            return TYPE_OWNER;
        }

        switch (chat.type) {
            case "1": {
                return TYPE_NORMAL;
            }

            case "6": {
                return TYPE_NOTICE;
            }
        }

        return super.getItemViewType(position);
    }


}
