package com.yjy998.ui.activity.main.my.business;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.JsonUtil;
import com.yjy998.R;
import com.yjy998.common.adapter.DealAdapter;
import com.yjy998.common.entity.ContractDetail;
import com.yjy998.common.entity.Deal;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.main.my.business.capital.BuySellFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * 成交
 */
public class DealFragment extends BusinessListFragment {

    @Override
    public String getTitle() {
        return ContextUtil.getString(R.string.deal);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refresh();
    }

    @Override
    public void refresh() {
        if (getActivity() instanceof BuySellFragment.ContractObserver) {
            ContractDetail contract = ((BuySellFragment.ContractObserver) getActivity()).getSharedContract();
            if (contract == null) {
                //没有选择合约
                return;
            }

            String contract_no = contract.contractId;
            SRequest request = new SRequest("http://www.yjy998.com/stock/deal");
            request.put("contract_no", contract_no);
            YHttpClient.getInstance().get(getActivity(), request, new YHttpHandler(false) {
                @Override
                protected void onStatusCorrect(Response response) {
                    try {
                        JSONArray array = new JSONArray(response.data);
                        DealAdapter adapter = (DealAdapter) getAdapter();
                        if (adapter==null){
                            adapter=new DealAdapter(getActivity(),new ArrayList<Deal>());
                            setAdapter(adapter);
                        }
                        JsonUtil.getArray(array,Deal.class,adapter.getData());
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}