package com.yjy998.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sp.lib.common.support.adapter.ViewHolderAdapter;
import com.yjy998.R;
import com.yjy998.entity.Stock;

import java.util.List;

public class StockListAdapter extends ViewHolderAdapter<Stock, TextView> {
    public StockListAdapter(Context context, List<Stock> data) {
        super(context, data, R.layout.list_item_stock);
    }

    @Override
    public TextView doFindIds(View convertView) {
        return (TextView) convertView;
    }

    @Override
    public void displayView(View convertView, TextView holder, int position) {
        Stock stock = getItem(position);
        holder.setText(stock.stockCode + " " + stock.stockName);
    }

}
