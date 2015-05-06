package com.yjy998.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;
import com.yjy998.entity.Contest;
import com.yjy998.entity.Contract;

import java.util.LinkedList;
import java.util.List;

public class ContractPagerAdapter extends PagerAdapter {

    private List<Contract> contracts;
    int pageCount;
    LinkedList<View> views = new LinkedList<View>();

    public ContractPagerAdapter(List<Contract> contracts) {
        this.contracts = contracts;
        if (contracts == null || contracts.isEmpty()) {
            pageCount = 0;
        } else {
            pageCount = (contracts.size() - 1) / 3 + 1;
        }
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewGroup convertView;
        ViewHolder holder;
        try {
            convertView = (ViewGroup) views.get(position);
            holder = (ViewHolder) convertView.getTag();
        } catch (IndexOutOfBoundsException e) {
            convertView = (ViewGroup) View.inflate(container.getContext(), R.layout.item_contract, null);

            holder = new ViewHolder();
            holder.item1 = convertView.findViewById(R.id.item1);
            holder.item2 = convertView.findViewById(R.id.item2);
            holder.item3 = convertView.findViewById(R.id.item3);
            convertView.setTag(holder);
            views.add(convertView);
        }

        int dataPosition = position * 3;
        initItem(holder.item1, dataPosition);
        initItem(holder.item2, dataPosition + 1);
        initItem(holder.item3, dataPosition + 2);

        container.addView(convertView);
        return convertView;
    }

    private void initItem(View item, int dataPosition) {
        try {
            Contract contract = contracts.get(dataPosition);
            TextView contractNo = (TextView) item.findViewById(R.id.contractNo);
            TextView typeText = (TextView) item.findViewById(R.id.typeText);
            contractNo.setText(contract.contract_no);
            typeText.setText("T+" + contract.type);
            item.setVisibility(View.VISIBLE);
        } catch (IndexOutOfBoundsException e) {
            item.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private class ViewHolder {
        View item1;
        View item2;
        View item3;
    }
}
