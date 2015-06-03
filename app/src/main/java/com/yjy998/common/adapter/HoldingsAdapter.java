package com.yjy998.common.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sp.lib.common.image.drawable.RoundDrawable;
import com.sp.lib.common.support.adapter.ViewHolderAdapter;
import com.yjy998.R;
import com.yjy998.common.entity.Holding;
import com.yjy998.ui.view.TwoTextItem;

import java.util.List;


public class HoldingsAdapter extends ViewHolderAdapter<Holding, Object> {

    private final int COLORS[] = new int[]{0XFFF7941D, 0XFF2DCBE4, 0XFFE0483D, 0XFF1452E3};


    public HoldingsAdapter(Context context, List<Holding> data) {
        super(context, data, R.layout.list_item_hodings);
    }

    @Override
    public Object doFindIds(View convertView) {
        ViewHolder holder = new ViewHolder();
        holder.indicator = (ImageView) convertView.findViewById(R.id.indicator);
        holder.stockName = (TextView) convertView.findViewById(R.id.stockName);
        holder.stockCode = (TextView) convertView.findViewById(R.id.stockCode);
        holder.titleLayout = (LinearLayout) convertView.findViewById(R.id.titleLayout);
        holder.stockAmount = (TwoTextItem) convertView.findViewById(R.id.stockAmount);
        holder.priceText = (TwoTextItem) convertView.findViewById(R.id.priceText);
        holder.marketValue = (TwoTextItem) convertView.findViewById(R.id.marketValue);
        holder.buyAmount = (TwoTextItem) convertView.findViewById(R.id.buyAmount);
        holder.costPrice = (TwoTextItem) convertView.findViewById(R.id.costPrice);
        holder.balanceRate = (TwoTextItem) convertView.findViewById(R.id.balanceRate);
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
        Holding holding = getItem(position);
        //当前价
        String price = "0";
        //后台来的数据被放大了100倍，在这里进行处理
        float realCost = holding.costBalance / 100;
        float realMarketValue = holding.marketValue / 100;

        if (holding.currentAmount != 0) {
            price = String.format("%.2f", realCost / holding.currentAmount);
        }

        //浮动盈亏
        String rateStr = "";

        if (realCost != 0) {
            float rateValue = (realMarketValue -realCost) / realCost * 100;
            rateStr = String.format("%.2f%%", rateValue);
            int color = convertView.getResources().getColor(rateValue > 0 ? R.color.textColorRed : R.color.textColorGreen);
            viewHolder.balanceRate.setTextColor(color);
            viewHolder.marketValue.setTextColor(color);
            viewHolder.priceText.setTextColor(color);
            viewHolder.buyAmount.setTextColor(color);
        }

        viewHolder.stockName.setText(holding.stockName);
        viewHolder.stockCode.setText(holding.stockCode);

        //第一行
        viewHolder.stockAmount.setText(holding.currentAmount + "");//持股数
        viewHolder.priceText.setText(price);
        viewHolder.balanceRate.setText(rateStr);

        //第二行
        viewHolder.marketValue.setText(realMarketValue + "");
        viewHolder.buyAmount.setText(holding.buyAmount + "");
        viewHolder.costPrice.setText(realCost + "");

        //第三行
    }


    private class ViewHolder {
        private ImageView indicator;
        private TextView stockName;
        private TextView stockCode;
        private LinearLayout titleLayout;
        private TwoTextItem stockAmount;
        private TwoTextItem priceText;
        private TwoTextItem marketValue;
        private TwoTextItem buyAmount;
        private TwoTextItem costPrice;
        private TwoTextItem balanceRate;
    }
}
