package com.yjy998.ui.activity.main.my.business;


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
import com.yjy998.ui.activity.main.my.business.capital.TradeFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 成交
 */
public class DealFragment extends BusinessListFragment {

    @Override
    public String getTitle() {
        return ContextUtil.getString(R.string.deal);
    }

    DealAdapter adapter;

    @Override
    public void refresh() {
        if (getActivity() instanceof TradeFragment.ContractObserver) {
            ContractDetail contract = ((TradeFragment.ContractObserver) getActivity()).getSharedContract();
            if (contract == null) {
                //没有选择合约
                return;
            }
            if (adapter == null) {
                adapter = new DealAdapter(getActivity(), new ArrayList<Deal>());
                setAdapter(adapter);
            }
            adapter.getData().clear();
            adapter.notifyDataSetChanged();
            String contract_no = contract.contractId;
            SRequest request = new SRequest("http://www.yjy998.com/stock/deal");
            request.put("contract_no", contract_no);
            YHttpClient.getInstance().get(getActivity(), request, new YHttpHandler(false) {
                @Override
                protected void onStatusCorrect(Response response) {
                    try {
                        JSONArray array = new JSONArray(response.data);
                        List<Deal> dealList = adapter.getData();
                        JsonUtil.getArray(array, Deal.class, dealList);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}
