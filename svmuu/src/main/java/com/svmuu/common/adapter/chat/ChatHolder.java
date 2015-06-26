package com.svmuu.common.adapter.chat;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.svmuu.common.adapter.BaseHolder;
import com.svmuu.common.entity.Chat;
import com.svmuu.ui.widget.ChatItemView;


public class ChatHolder extends BaseHolder {
    ChatItemView itemView;

    public ChatHolder(ChatItemView itemView) {
        super(itemView);
        this.itemView=itemView;
    }

    public void setData(Chat chat) {
        ChatItemView.MsgLayoutInfo info = itemView.getInfo();
//        itemView.setMsgType(chat.isSelf);
        info.timeText.setText(chat.time);
        info.nameText.setText(chat.name);
        info.contentText.setText(chat.msg);
        if (TextUtils.isEmpty(chat.job)){
            info.jobText.setVisibility(View.INVISIBLE);
        }else{
            info.jobText.setVisibility(View.VISIBLE);
            info.jobText.setText(chat.job);
        }
    }

    /**
     * 将加载头像但操作留给Adapter，公用一个DisplayImageOptions,减少创建，从而提高性能
     */
    public ImageView getAvatarView() {
        return itemView.getInfo().avatarView;
    }
}
