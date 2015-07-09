package com.svmuu.common.adapter.chat.holders;

import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.svmuu.R;
import com.svmuu.common.entity.Chat;

/**
 * Created by user1 on 2015/7/9.
 * 普通聊天
 */
public class ChatNormalHolder extends ChatHolderImpl {
    protected ChatNormalHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void displayWith(Chat chat,DisplayImageOptions options) {
        super.displayWith(chat,options);
        if ("1".equals(chat.is_admin)) {
            chatItemJob.setVisibility(View.VISIBLE);
            chatItemJob.setText(itemView.getContext().getString(R.string.manager));
        } else {
            chatItemJob.setVisibility(View.INVISIBLE);
        }
    }
}
