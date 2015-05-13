package com.yjy998.common.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yjy998.R;
import com.yjy998.common.entity.Stock;

import java.util.ArrayList;
import java.util.List;

public class CapitalBuySellAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private boolean isBuy = true;
    private Stock stock;
    String[] numbers;
    private String titlePrefix1;
    private String titlePrefix2;

    public CapitalBuySellAdapter(Context context, Stock stock) {
        inflater = LayoutInflater.from(context);
        numbers = context.getResources().getStringArray(R.array.numbers);
        this.stock = stock;

    }

    public void setStock(Stock stock) {
        this.stock = stock;
        notifyDataSetChanged();
    }

    public void setBuy(boolean isBuy) {
        this.isBuy = isBuy;
        titlePrefix1 = isBuy ? "买" : "卖";
        titlePrefix2 = isBuy ? "卖" : "买";
    }

    @Override
    public int getCount() {
        return 11;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_capital_info, null);
            convertView.setTag(holder);
            holder.numberText = (TextView) convertView.findViewById(R.id.numberText);
            holder.amountText = (TextView) convertView.findViewById(R.id.amountText);
            holder.priceText = (TextView) convertView.findViewById(R.id.priceText);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String title = "";
        if (position < 5) {
            title = titlePrefix1 + numbers[position];
        } else if (position > 5) {
            title = titlePrefix2 + numbers[10 - position];
        }
        Item item;

        if (stock == null) {
            item = new Item(title, "-----", "-----");
        } else {

            int fixedPosition = isBuy ? position : 10 - position;

            switch (fixedPosition) {
                case 0:
                    item = new Item(title, stock.buyCount1, stock.buyPrice1);
                    break;
                case 1:
                    item = new Item(title, stock.buyCount2, stock.buyPrice2);
                    break;
                case 2:
                    item = new Item(title, stock.buyCount3, stock.buyPrice3);
                    break;
                case 3:
                    item = new Item(title, stock.buyCount4, stock.buyPrice4);
                    break;
                case 4:
                    item = new Item(title, stock.buyCount5, stock.buyPrice5);
                    break;

                //分割
                case 6:
                    item = new Item(title, stock.sellCount5, stock.sellPrice5);
                    break;
                case 7:
                    item = new Item(title, stock.sellCount4, stock.sellPrice4);
                    break;
                case 8:
                    item = new Item(title, stock.sellCount3, stock.sellPrice3);
                    break;
                case 9:
                    item = new Item(title, stock.sellCount2, stock.sellPrice2);
                    break;
                case 10:
                    item = new Item(title, stock.sellCount1, stock.sellPrice1);
                    break;
                default:
                    item = null;
            }
        }


        //中间空出来一行
        if (TextUtils.isEmpty(title)) {
            convertView.setVisibility(View.INVISIBLE);
        } else {
            convertView.setVisibility(View.VISIBLE);
            holder.numberText.setText(item.tittle);
            holder.amountText.setText(item.count);
            holder.priceText.setText(item.price);

        }
        return convertView;
    }

    private class ViewHolder {
        TextView numberText;
        TextView amountText;
        TextView priceText;
    }

    private class Item {
        String tittle;
        String count;
        String price;

        private Item(String tittle, String count, String price) {
            this.tittle = tittle;
            this.count = count;
            this.price = price;
        }
    }
}
