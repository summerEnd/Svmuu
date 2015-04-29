package com.yjy998.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yjy998.R;

public class CapitalBuySellAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private String[] numbers;
    private boolean isBuy = true;

    public CapitalBuySellAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        numbers = context.getResources().getStringArray(R.array.numbers);
    }

    public void setBuy(boolean isBuy) {
        this.isBuy = isBuy;
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
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String numberText = "";
        if (isBuy) {

            if (position < 5) {
                numberText = "买" + numbers[position % 5];
            } else if (position > 5) {
                numberText = "卖" + numbers[10 - position];
            }

        } else {
            if (position < 5) {
                numberText = "卖" + numbers[position % 5];
            } else if (position > 5) {
                numberText = "买" + numbers[10 - position];
            }
        }
        if (TextUtils.isEmpty(numberText)) {
            convertView.setVisibility(View.INVISIBLE);
        } else {
            convertView.setVisibility(View.VISIBLE);
            holder.numberText.setText(numberText);

        }
        return convertView;
    }

    private class ViewHolder {
        TextView numberText;
    }
}
