package com.sp.lib.activity.album;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sp.lib.R;

import java.util.List;

public class PhotoDirAdapter extends BaseAdapter{
    DisplayImageOptions options;
    Context context;
    private List<PhotoDirInfo> dirs;
    int selectedItem;
    public PhotoDirAdapter(Context context, List<PhotoDirInfo> dirs) {
        this.context = context;
        this.dirs = dirs;
        options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
    }

    public void select(int position) {
        selectedItem = position;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return dirs.size();
    }

    @Override
    public Object getItem(int position) {
        return dirs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.photo_album_window_list_item, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.dir_name = (TextView) convertView.findViewById(R.id.dir_name);
            holder.num_pics = (TextView) convertView.findViewById(R.id.num_pics);
            holder.selected = (ImageView) convertView.findViewById(R.id.selected);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PhotoDirInfo info=dirs.get(position);
        List<String> list = info.photos;
        if (list.size() > 0) {
            String image = list.get(0);
            ImageLoader.getInstance().displayImage("file://" + image, holder.imageView, options);
        }
        holder.dir_name.setText(info.dirName);
        holder.num_pics.setText(list.size() + "张");
        holder.selected.setVisibility(selectedItem == position ? View.VISIBLE : View.INVISIBLE);
        convertView.setTag(holder);
        return convertView;
    }


    public static class PhotoDirInfo {
        public String dirName;
        public List<String> photos;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView dir_name;
        TextView num_pics;
        ImageView selected;
    }
}
