package com.yjy998.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sp.lib.common.support.adapter.ViewHolderAdapter;
import com.yjy998.R;
import com.yjy998.entity.Holding;

import java.util.List;

public class ContractStockListAdapter extends ViewHolderAdapter<Holding, Object> {


    public ContractStockListAdapter(Context context, List<Holding> data) {
        super(context, data, R.layout.contract_stock_info_layout);
    }

    @Override
    public Object doFindIds(View convertView) {
        ViewHolder holder = new ViewHolder();
        holder.stockName = (TextView) convertView.findViewById(R.id.stockName);
        holder.floatBalance = (TextView) convertView.findViewById(R.id.floatBalance);
        holder.holdNumber = (TextView) convertView.findViewById(R.id.holdNumber);
        holder.holdValue = (TextView) convertView.findViewById(R.id.holdValue);
        return holder;
    }

    @Override
    public void displayView(View convertView, Object holder, int position) {
        ViewHolder viewHolder= (ViewHolder) holder;
        Holding holding =getItem(position);
//        viewHolder.stockName.setText(hold.);
//        viewHolder.floatBalance.setText(stock.turnOverRate);
//        viewHolder.holdNumber.setText(stock.);
    }

    private class ViewHolder {
        private TextView stockName;
        private TextView floatBalance;
        private TextView holdNumber;
        private TextView holdValue;
    }

}
