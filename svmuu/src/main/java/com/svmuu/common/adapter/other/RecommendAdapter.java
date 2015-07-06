package com.svmuu.common.adapter.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sp.lib.common.support.adapter.ViewHolderAdapter;
import com.svmuu.R;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.Tests;
import com.svmuu.common.entity.CircleMaster;

import java.util.List;

public class RecommendAdapter extends ViewHolderAdapter<CircleMaster, Object> {
    DisplayImageOptions options;

    public RecommendAdapter(Context context, List<CircleMaster> data) {
        super(context, data, R.layout.item_recommand);
        options=ImageOptions.getRoundCorner(5);
    }


    @Override
    public Object doFindIds(View convertView) {
        ViewHolder holder = new ViewHolder();

        holder.avatarImage = (ImageView) convertView.findViewById(R.id.avatarImage);
        holder.nickText = (TextView) convertView.findViewById(R.id.nickText);
        holder.introText = (TextView) convertView.findViewById(R.id.introText);
        holder.fansText = (TextView) convertView.findViewById(R.id.fansText);
        return holder;
    }

    @Override
    public void displayView(View convertView, Object holder, int position) {
        ViewHolder viewHolder= (ViewHolder) holder;
        CircleMaster master=getData().get(position);
        viewHolder.fansText.setText(master.Hot);
        viewHolder.introText.setText(master.desc);
        viewHolder.nickText.setText(master.name);
        ImageLoader.getInstance().displayImage(master.uface, viewHolder.avatarImage,options);
    }



    private class ViewHolder {
        private ImageView avatarImage;
        private TextView nickText;
        private TextView introText;
        private TextView fansText;
    }
}
