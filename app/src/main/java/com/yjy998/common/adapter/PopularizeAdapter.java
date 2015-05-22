package com.yjy998.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sp.lib.common.support.adapter.ViewHolderAdapter;
import com.yjy998.R;
import com.yjy998.common.entity.Popularize;

import java.util.List;

public class PopularizeAdapter extends BaseAdapter {
    LayoutInflater inflater;
    List<Popularize> data;
    View emptyView;

    public PopularizeAdapter(Context context, List<Popularize> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
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
            emptyView = inflater.inflate(R.layout.empty_layout, null);
            ((TextView) emptyView.findViewById(R.id.noDataText)).setText(R.string.not_invite_any);
            emptyView.setPadding(0,100,0,0);
            return emptyView;
        }
        ViewHolder viewHolder;

        if (convertView == null || convertView == emptyView) {
            convertView = inflater.inflate(R.layout.list_item_popularize, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }

    private class ViewHolder {

    }
}

