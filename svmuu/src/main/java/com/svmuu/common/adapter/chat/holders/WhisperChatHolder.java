package com.svmuu.common.adapter.chat.holders;

import android.text.Html;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.sp.lib.common.util.ContextUtil;
import com.svmuu.common.entity.Chat;
import com.svmuu.ui.activity.live.ChatFragment;

/**
 * Created by user1 on 2015/7/9.
 * 铁粉悄悄话
 */
public class WhisperChatHolder extends MasterChatHolder {
    protected WhisperChatHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void displayWith(Chat chat, DisplayImageOptions options) {
        super.displayWith(chat, options);

    }

    @Override
    public void displayContent(Chat chat, Html.ImageGetter imageGetter) {
        //1、普通粉丝；2 铁粉；3 年粉 4 季粉',
        String fans_type = ChatFragment.getSharedChatParams().fan_type;
        if ("2".equals(fans_type) || "3".equals(fans_type) || "4".equals(fans_type)) {
            super.displayContent(chat, imageGetter);
        } else {
            //既不是铁粉也不是年粉
            String click = "";
            String msg = "本条信息为铁粉悄悄话，加入铁粉后即可查看！";
            SpannableString spannableString = new SpannableString(msg + click);
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    ContextUtil.toast("");
                }
            }, msg.length(), spannableString.length(), 0);
            chatItemContent.setText(spannableString);
        }
    }
}
