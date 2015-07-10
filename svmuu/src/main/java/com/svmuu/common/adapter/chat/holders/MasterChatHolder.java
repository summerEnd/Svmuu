package com.svmuu.common.adapter.chat.holders;

import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.svmuu.R;
import com.svmuu.common.entity.Chat;

public class MasterChatHolder extends ChatHolderImpl {
    public MasterChatHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void displayWith(Chat chat, DisplayImageOptions options) {
        super.displayWith(chat, options);
        chatItemJob.setVisibility(View.VISIBLE);
        chatItemJob.setText(itemView.getContext().getString(R.string.circleMaster));
    }
}
