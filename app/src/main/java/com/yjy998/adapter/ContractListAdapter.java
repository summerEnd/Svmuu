package com.yjy998.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sp.lib.common.support.adapter.ViewHolderAdapter;
import com.yjy998.R;
import com.yjy998.entity.Contract;

import java.util.List;

public class ContractListAdapter extends ViewHolderAdapter<Contract, Object> {


    public ContractListAdapter(Context context, List<Contract> data) {
        super(context, data, R.layout.list_item_contract);

    }

    @Override
    public Object doFindIds(View convertView) {
        ViewHolder holder = new ViewHolder();
        holder.listContractText = (TextView) convertView.findViewById(R.id.listContractText);

        return holder;
    }

    @Override
    public void displayView(View convertView, Object holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Contract contract = getItem(position);
        viewHolder.listContractText.setText(getContext().getString(R.string.contract_s1_s2,contract.contract_type,contract.id));
    }

    private class ViewHolder {
        TextView listContractText;
    }
}
