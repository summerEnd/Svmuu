package com.yjy998.ui.activity.my.business;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.JsonUtil;
import com.yjy998.R;
import com.yjy998.adapter.DealAdapter;
import com.yjy998.adapter.HoldingsAdapter;
import com.yjy998.entity.Contract;
import com.yjy998.entity.ContractDetail;
import com.yjy998.entity.Deal;
import com.yjy998.entity.Holding;
import com.yjy998.http.Response;
import com.yjy998.http.YHttpClient;
import com.yjy998.http.YHttpHandler;
import com.yjy998.ui.activity.my.business.capital.BuySellFragment;
import com.yjy998.ui.pop.CenterPopup;

import org.json.JSONArray;
import org.json.JSONException;

import static com.yjy998.ui.pop.CenterPopup.PopItem;
import static com.yjy998.ui.pop.CenterPopup.PopWidget;

/**
 * 成交
 */
public class DealFragment extends BusinessListFragment {

    @Override
    public String getTitle() {
        return ContextUtil.getString(R.string.deal);
    }

    @Override
    protected void onCreatePop(PopWidget popWidget) {
        popWidget.add(new CenterPopup.PopItem(0, getString(R.string.buyIn), getResources().getColor(R.color.roundButtonBlue)));
        popWidget.add(new CenterPopup.PopItem(1, getString(R.string.sellOut), getResources().getColor(R.color.roundButtonRed)));
    }

    @Override
    protected void onPopItemClick(PopItem item) {
        super.onPopItemClick(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refresh();
    }

    @Override
    public void refresh() {
        if (getActivity() instanceof BuySellFragment.ContractObserver) {
            ContractDetail contract = ((BuySellFragment.ContractObserver) getActivity()).getContract();
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
                        DealAdapter adapter = new DealAdapter(getActivity(), JsonUtil.getArray(array, Deal.class));
                        setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}
