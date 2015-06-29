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

public class ChatAdapter extends BaseAdapter<Chat,ChatHolder> {
    DisplayImageOptions options;

    public ChatAdapter(@NonNull Context context,List<Chat> data) {
        super(context,data);
        options= ImageOptions.getRoundCorner(4);
    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatHolder(new ChatItemView(getContext(), viewType==0));
    }

    @Override
    public int getItemViewType(int position) {
        return "1".equals(getData().get(position).is_owner)?0:1;
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        holder.setData(getData().get(position));
        ImageLoader.getInstance().displayImage(getData().get(position).uface,holder.getAvatarView(),options);
    }

}
