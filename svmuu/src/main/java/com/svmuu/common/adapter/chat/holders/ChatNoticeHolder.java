package com.svmuu.common.adapter.chat.holders;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;

import com.svmuu.common.entity.Chat;

/**
 * Created by user1 on 2015/7/9.
 * 系统公告
 */
public class ChatNoticeHolder extends ChatHolderImpl {
    protected ChatNoticeHolder(View itemView) {
        super(itemView);
        //暂时隐藏系统公告
        itemView.setVisibility(View.GONE);
        if (chatItemContent != null) {
//            chatItemContent.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    @Override
    public void displayContent(Chat chat, Html.ImageGetter imageGetter,Html.TagHandler handler) {
        super.displayContent(chat, imageGetter,handler);
    }
}
