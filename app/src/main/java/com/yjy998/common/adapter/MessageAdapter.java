package com.yjy998.common.adapter;

import android.content.Context;
import android.view.View;

import com.sp.lib.common.support.adapter.ViewHolderAdapter;
import com.yjy998.R;
import com.yjy998.common.entity.Message;

import java.util.List;

public class MessageAdapter extends ViewHolderAdapter<Message, Object> {
    public MessageAdapter(Context context, List<Message> data) {
        super(context, data, R.layout.list_item_msg);
    }

    @Override
    public Message doFindIds(View convertView) {
        return null;
    }

    @Override
    public void displayView(View convertView, Object holder, int position) {

    }

}
