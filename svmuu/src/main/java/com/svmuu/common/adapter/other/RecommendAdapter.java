package com.svmuu.common.adapter.other;

import android.content.Context;
import android.content.Intent;
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
import com.svmuu.ui.activity.live.LiveActivity;

import java.util.List;

public class RecommendAdapter extends ViewHolderAdapter<CircleMaster, Object> implements View.OnClickListener {
    DisplayImageOptions options;

    public RecommendAdapter(Context context, List<CircleMaster> data) {
        super(context, data, R.layout.item_recommand);
        options = ImageOptions.getRoundCorner(5);
    }

    @Override
    public Object doFindIds(View convertView) {
        ViewHolder holder = new ViewHolder();

        holder.avatarImage = (ImageView) convertView.findViewById(R.id.avatarImage);
        holder.nickText = (TextView) convertView.findViewById(R.id.nickText);
        holder.introText = (TextView) convertView.findViewById(R.id.introText);
        holder.fansText = (TextView) convertView.findViewById(R.id.fansText);
        holder.toggle = (TextView) convertView.findViewById(R.id.toggle);

        holder.avatarImage.setOnClickListener(jumpListener);
        holder.nickText.setOnClickListener(jumpListener);
        holder.introText.setOnClickListener(this);
        holder.toggle.setOnClickListener(this);
        return holder;
    }

    @Override
    public void displayView(View convertView, Object holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        CircleMaster master = getData().get(position);
        viewHolder.fansText.setText(master.Hot);
        viewHolder.introText.setText(master.desc);
        viewHolder.nickText.setText(master.name);
        if (master.isOPen) {
            viewHolder.toggle.setText("收缩");
            viewHolder.introText.setSingleLine(false);
        } else {
            viewHolder.toggle.setText("展开");
            viewHolder.introText.setSingleLine(true);
        }

        viewHolder.toggle.setTag(master);
        viewHolder.introText.setTag(master);

        viewHolder.avatarImage.setTag(master);
        viewHolder.nickText.setTag(master);

        ImageLoader.getInstance().displayImage(master.uface, viewHolder.avatarImage, options);
    }

    @Override
    public void onClick(View v) {
        CircleMaster master = (CircleMaster) v.getTag();
        master.isOPen = !master.isOPen;
        notifyDataSetChanged();
    }

    private View.OnClickListener jumpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CircleMaster master = (CircleMaster) v.getTag();
            Context context = v.getContext();
            context.startActivity(new Intent(context, LiveActivity.class)
                    .putExtra(LiveActivity.EXTRA_QUANZHU_ID, master.uid));
        }
    };

    private class ViewHolder {
        private ImageView avatarImage;
        private TextView nickText;
        private TextView introText;
        private TextView fansText;
        private TextView toggle;
    }
}
