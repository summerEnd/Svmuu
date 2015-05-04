package com.sp.lib.activity.album;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sp.lib.R;
import com.sp.lib.common.util.DisplayUtil;

import java.util.List;


/**
 * 照片
 */
public class PhotoGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> photos;
    DisplayImageOptions options;
    View header;
    int width;

    @SuppressWarnings("deprecation")
    public PhotoGridAdapter(Context context, List<String> photos) {
        this.mContext = context;
        this.photos = photos;

        options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnFail(R.drawable.image_failed)
                .showImageForEmptyUri(R.drawable.image_failed)
                .build();
        header = LayoutInflater.from(mContext).inflate(R.layout.camera_header, null);
        float screenWidth = DisplayUtil.getScreenWidth(context);
        width = (int) (screenWidth / 3 - DisplayUtil.dp(1, context.getResources()));
        header.setLayoutParams(new AbsListView.LayoutParams(width, width));
    }

    public void setData(List<String> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return photos.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return "";
        } else {
            return photos.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            convertView = header;
            header.setTag(null);
        } else {
            ViewHolder holder;
            if (convertView == null || convertView.getTag() == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_photo, null);
                holder.imageView = (ImageView) convertView.findViewById(R.id.image);
                holder.imageView.setLayoutParams(new AbsListView.LayoutParams(width, width));
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String imageUrl = photos.get(position - 1);
            ImageLoader.getInstance().displayImage("file://" + imageUrl, holder.imageView, options);
            convertView.setTag(holder);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}
