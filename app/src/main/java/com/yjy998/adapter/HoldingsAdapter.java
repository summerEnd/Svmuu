package com.yjy998.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.sp.lib.common.image.drawable.RoundDrawable;
import com.sp.lib.common.support.adapter.ViewHolderAdapter;
import com.yjy998.R;
import com.yjy998.entity.Hold;

import java.util.List;


public class HoldingsAdapter extends ViewHolderAdapter<Hold, Object> {

    private final int COLORS[] = new int[]{0XFFF7941D, 0XFF2DCBE4, 0XFFE0483D, 0XFF1452E3};

    public HoldingsAdapter(Context context, List<Hold> data) {
        super(context, data, R.layout.list_item_hold);
    }

    @Override
    public Object doFindIds(View convertView) {
        ViewHolder holder = new ViewHolder();
        holder.indicator = (ImageView) convertView.findViewById(R.id.indicator);
        return holder;
    }

    @Override
    public void displayView(View convertView, Object holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        Drawable d = mHolder.indicator.getDrawable();

        if (d == null || !(d instanceof RoundDrawable)) {
            d = new RoundDrawable(COLORS[position % COLORS.length], 10);
        } else {
            ((RoundDrawable) d).setColor(COLORS[position % COLORS.length]);
        }

        mHolder.indicator.setImageDrawable(d);
    }

    private class ViewHolder {
        ImageView indicator;
    }
}
