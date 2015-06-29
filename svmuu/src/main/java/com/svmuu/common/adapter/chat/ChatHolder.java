package com.svmuu.common.adapter.chat;

import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.svmuu.common.adapter.BaseHolder;
import com.svmuu.common.entity.Chat;
import com.svmuu.ui.widget.ChatItemView;

import org.xml.sax.XMLReader;


public class ChatHolder extends BaseHolder {
    ChatItemView itemView;

    public ChatHolder(ChatItemView itemView) {
        super(itemView);
        this.itemView=itemView;
    }

    public void setData(Chat chat) {
        ChatItemView.MsgLayoutInfo info = itemView.getInfo();
//        itemView.setMsgType(chat.isSelf);
        info.timeText.setText(chat.time_m);
        info.nameText.setText(chat.uname);
        info.contentText.setText(Html.fromHtml(chat.content));
        if ("1".equals(chat.is_admin)){
            info.jobText.setText("管理员");
        }else{
            info.jobText.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 将加载头像但操作留给Adapter，公用一个DisplayImageOptions,减少创建，从而提高性能
     */
    public ImageView getAvatarView() {
        return itemView.getInfo().avatarView;
    }
}
