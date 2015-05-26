package com.yjy998.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yjy998.R;
import com.yjy998.common.entity.Popularize;

import java.util.List;

public class PopularizeFriendsAdapter extends BaseAdapter {
    LayoutInflater inflater;
    List<Popularize> data;
    View emptyView;


    public PopularizeFriendsAdapter(Context context, List<Popularize> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        emptyView = inflater.inflate(R.layout.empty_layout, null);
        ((TextView) emptyView.findViewById(R.id.noDataText)).setText(R.string.not_invite_any);
        emptyView.setPadding(0, 100, 0, 0);
    }

    public List<Popularize> getData() {
        return data;
    }

    @Override
    public int getCount() {


        if (data == null || data.size() == 0) {
            return 1;
        }

        return data.size();
    }

    @Override
    public Object getItem(int position) {
        if (data == null || data.size() == 0) {
            return null;
        }
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (data == null || data.size() == 0) {

            return emptyView;
        }
        ViewHolder viewHolder;
        Popularize item = data.get(position);
        if (convertView == null || convertView == emptyView) {
            convertView = inflater.inflate(R.layout.list_item_popularize_friend, null);
            viewHolder = new ViewHolder();
            viewHolder.nameText = (TextView) convertView.findViewById(R.id.nameText);
            viewHolder.dateText = (TextView) convertView.findViewById(R.id.dateText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.nameText.setText(item.unick);
        viewHolder.dateText.setText(item.inviteTime);
        return convertView;
    }

    private class ViewHolder {
        private TextView nameText;
        private TextView dateText;
    }
}

