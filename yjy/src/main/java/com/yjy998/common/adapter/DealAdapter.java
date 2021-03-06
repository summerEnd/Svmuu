package com.yjy998.common.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sp.lib.common.image.drawable.RoundDrawable;
import com.sp.lib.common.support.adapter.ViewHolderAdapter;
import com.yjy998.R;
import com.yjy998.common.entity.Deal;
import com.yjy998.common.util.DateUtil;
import com.yjy998.ui.view.TwoTextItem;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DealAdapter extends ViewHolderAdapter<Deal, Object> {

    private final int COLORS[] = new int[]{0XFFF7941D, 0XFF2DCBE4, 0XFFE0483D, 0XFF1452E3};



    public DealAdapter(Context context, List<Deal> data) {
        super(context, data, R.layout.list_item_deal);
    }

    @Override
    public Object doFindIds(View convertView) {
        ViewHolder holder = new ViewHolder();

        holder.indicator = (ImageView) convertView.findViewById(R.id.indicator);
        holder.stockName = (TextView) convertView.findViewById(R.id.stockName);
        holder.stockCode = (TextView) convertView.findViewById(R.id.stockCode);
        holder.titleLayout = (LinearLayout) convertView.findViewById(R.id.titleLayout);
        holder.tradeType = (TwoTextItem) convertView.findViewById(R.id.tradeType);
        holder.priceText = (TwoTextItem) convertView.findViewById(R.id.priceText);
        holder.countText = (TwoTextItem) convertView.findViewById(R.id.countText);
        holder.dealTime = (TextView) convertView.findViewById(R.id.dealTime);
        holder.dealPrice = (TextView) convertView.findViewById(R.id.dealPrice);
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
        Deal deal = getItem(position);

        viewHolder.priceText.setText(deal.businessPrice );

        viewHolder.stockName.setText(deal.stockName);
        viewHolder.stockCode.setText(deal.stockCode);
        viewHolder.tradeType.setText(deal.entrustDirection.equals("1") ? R.string.buyIn : R.string.sellOut);
        viewHolder.countText.setText(deal.businessAmount);

        viewHolder.dealTime.setText(convertView.getResources().getString(R.string.dealTime_s, DateUtil.formatDate(deal.businessTime)));
        viewHolder.dealPrice.setText(convertView.getResources().getString(R.string.dealPrice_s, new BigDecimal(deal.businessPrice).multiply(new BigDecimal("100")).floatValue() + ""));

    }

    private void initialize() {

    }

    private class ViewHolder {

        ImageView indicator;
        TextView stockName;
        TextView stockCode;
        LinearLayout titleLayout;
        TwoTextItem tradeType;
        TwoTextItem priceText;
        TwoTextItem countText;
        TextView dealTime;
        TextView dealPrice;
    }
}
