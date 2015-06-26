package com.svmuu.common.adapter.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.svmuu.R;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.Tests;
import com.svmuu.common.adapter.BaseAdapter;
import com.svmuu.common.entity.Chat;
import com.svmuu.ui.widget.ChatItemView;

import java.util.List;

public class ChatAdapter extends BaseAdapter<ChatHolder> {
    DisplayImageOptions options;
    List<Chat> data;

    public ChatAdapter(@NonNull Context context,List<Chat> data) {
        super(context);
        this.data=data;
        int avatarSize=context.getResources().getDimensionPixelSize(R.dimen.avatarSize);
        options= ImageOptions.getRoundCorner(4);
    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatHolder(new ChatItemView(getContext(), viewType==0));
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).isSelf?0:1;
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        holder.setData(data.get(position));
        ImageLoader.getInstance().displayImage(Tests.IMAGE,holder.getAvatarView(),options);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
