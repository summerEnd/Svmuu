package com.yjy998.common.adapter;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sp.lib.common.support.adapter.ViewHolderAdapter;
import com.yjy998.R;
import com.yjy998.common.entity.Rank;

import java.util.List;

public class RankAdapter extends ViewHolderAdapter<Rank, Object> {

    final int RED;
    final int BLACK;

    public RankAdapter(Context context, List<Rank> data) {
        super(context, data, R.layout.list_item_rank);
        RED = context.getResources().getColor(R.color.textColorRed);
        BLACK = context.getResources().getColor(R.color.textColorBlack);
    }

    @Override
    public Object doFindIds(View convertView) {
        ViewHolder holder = new ViewHolder();
        holder.rankText = (TextView) convertView.findViewById(R.id.rankText);
        holder.rankImage = (ImageView) convertView.findViewById(R.id.rankImage);
        holder.nameText = (TextView) convertView.findViewById(R.id.nameText);
        holder.profitText = (TextView) convertView.findViewById(R.id.profitText);
        return holder;
    }

    @Override
    public void displayView(View convertView, Object holder, int position) {
        ViewHolder vHolder = (ViewHolder) holder;
        Rank rank = getItem(position);
        switch (position) {
            case 0: {
                showRankImage(vHolder, R.drawable.ic_no1);
                break;
            }
            case 1: {
                showRankImage(vHolder, R.drawable.ic_no2);
                break;
            }
            case 2: {
                showRankImage(vHolder, R.drawable.ic_no3);
                break;
            }
            default: {
                showRankImage(vHolder, 0);
                vHolder.rankText.setText(String.valueOf(position + 1));
            }
        }



        vHolder.profitText.setText(convertView.getContext().getString(R.string.rate_f, (float)rank.rate * 1000));
        vHolder.nameText.setText(rank.nick);
    }

    private void showRankImage(ViewHolder holder, int image) {
        if (image != 0) {
            holder.rankText.setVisibility(View.INVISIBLE);
            holder.rankImage.setVisibility(View.VISIBLE);
            holder.profitText.setTextColor(RED);
            holder.rankImage.setImageResource(image);
        } else {
            holder.rankText.setVisibility(View.VISIBLE);
            holder.rankImage.setVisibility(View.INVISIBLE);
            holder.profitText.setTextColor(BLACK);
        }

    }

    private class ViewHolder {
        TextView rankText;
        ImageView rankImage;
        TextView nameText;
        TextView profitText;
    }
}
