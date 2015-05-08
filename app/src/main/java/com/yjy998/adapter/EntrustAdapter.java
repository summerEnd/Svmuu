package com.yjy998.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sp.lib.common.image.drawable.RoundDrawable;
import com.sp.lib.common.support.adapter.ViewHolderAdapter;
import com.yjy998.R;
import com.yjy998.entity.Entrust;
import com.yjy998.entity.Holding;
import com.yjy998.ui.view.TwoTextItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class EntrustAdapter extends ViewHolderAdapter<Entrust, Object> {

    private final int COLORS[] = new int[]{0XFFF7941D, 0XFF2DCBE4, 0XFFE0483D, 0XFF1452E3};

    SimpleDateFormat parser = new SimpleDateFormat("hhMMss");
    SimpleDateFormat format = new SimpleDateFormat("hh:MM:ss");

    public EntrustAdapter(Context context, List<Entrust> data) {
        super(context, data, R.layout.list_item_entrust);
    }

    @Override
    public Object doFindIds(View convertView) {
        ViewHolder holder = new ViewHolder();
        holder.indicator = (ImageView) convertView.findViewById(R.id.indicator);
        holder.countText = (TwoTextItem) convertView.findViewById(R.id.countText);
        holder.priceText = (TwoTextItem) convertView.findViewById(R.id.priceText);
        holder.entrustStatus = (TwoTextItem) convertView.findViewById(R.id.entrustStatus);
        holder.stockName = (TextView) convertView.findViewById(R.id.stockName);
        holder.tradeType = (TwoTextItem) convertView.findViewById(R.id.tradeType);
        holder.stockCode = (TextView) convertView.findViewById(R.id.stockCode);
        holder.entrustTime = (TwoTextItem) convertView.findViewById(R.id.entrustTime);
        return holder;
    }

    @Override
    public void displayView(View convertView, Object holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Drawable d = viewHolder.indicator.getDrawable();

        if (d == null || !(d instanceof RoundDrawable)) {
            d = new RoundDrawable(COLORS[position % COLORS.length], 10);
        } else {
            ((RoundDrawable) d).setColor(COLORS[position % COLORS.length]);
        }

        viewHolder.indicator.setImageDrawable(d);
        Entrust entrust = getItem(position);

        viewHolder.priceText.setText(entrust.entrustPrice);

        viewHolder.stockName.setText(entrust.stockName);
        viewHolder.stockCode.setText(entrust.stockCode);
        viewHolder.tradeType.setText(entrust.entrustDirection.equals("1") ? R.string.buyIn : R.string.sellOut);
        viewHolder.countText.setText(entrust.entrustAmount);
        viewHolder.entrustStatus.setText(entrust.getEntrustStatus());
        String time;
        try {
            Date date = parser.parse(entrust.entrustTime);
            time=format.format(date);
        } catch (ParseException e) {
            time="";
        }
        viewHolder.entrustTime.setText(time);

    }


    private class ViewHolder {
        ImageView indicator;
        TwoTextItem countText;
        TwoTextItem priceText;
        TextView stockName;
        TwoTextItem entrustTime;
        TwoTextItem tradeType;
        TwoTextItem entrustStatus;
        TextView stockCode;
    }
}
