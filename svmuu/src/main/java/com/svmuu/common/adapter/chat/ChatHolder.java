package com.svmuu.common.adapter.chat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gensee.view.MyTextViewEx;
import com.svmuu.R;
import com.svmuu.common.adapter.BaseHolder;
import com.svmuu.common.entity.Chat;
import com.svmuu.ui.widget.ChatItemView;

import org.xml.sax.XMLReader;


public class ChatHolder extends BaseHolder {
    ChatItemView itemView;

    public ChatHolder(ChatItemView itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public void setData(Chat chat) {
        Context context = itemView.getContext();

        ChatItemView.MsgLayoutInfo info = itemView.getInfo();
        //        itemView.setMsgType(chat.isSelf);
        info.timeText.setText(chat.time_m);
        info.nameText.setText(chat.uname);


        if ("1".equals(chat.is_admin)) {
            info.jobText.setVisibility(View.VISIBLE);
            info.jobText.setText(context.getString(R.string.manager));
        } else if ("1".equals(chat.is_owner)) {
            info.jobText.setVisibility(View.VISIBLE);
            info.jobText.setText(context.getString(R.string.circleMaster));
        } else {
            info.jobText.setVisibility(View.INVISIBLE);
        }
        //1、普通粉丝；2 铁粉；3 年粉 4 季粉',
        info.fansIcon.setVisibility(View.VISIBLE);
        switch (chat.fans_type) {
            case "4":
            case "2": {
                info.fansIcon.setImageResource(R.drawable.iron_fans);
                break;
            }
            case "3": {
                info.fansIcon.setImageResource(R.drawable.year_fans);
                break;
            }

            default: {
                info.fansIcon.setVisibility(View.GONE);
            }
        }

        //消息类型 1 普通 2 解盘 3 问答 4 悄悄话 5 公告',
        switch (chat.type) {

            case "5": {
                ((View) info.fansIcon.getParent()).setVisibility(View.GONE);
                break;
            }
            default: {
                ((View) info.fansIcon.getParent()).setVisibility(View.VISIBLE);
            }
        }


    }

    /**
     * 将加载头像但操作留给Adapter，公用一个DisplayImageOptions,减少创建，从而提高性能
     */
    public ImageView getAvatarView() {
        return itemView.getInfo().avatarView;
    }

    public MyTextViewEx getContentTextView(){
        return itemView.getInfo().contentText;
    }
}
