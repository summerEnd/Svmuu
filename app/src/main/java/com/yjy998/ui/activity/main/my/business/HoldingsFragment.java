package com.yjy998.ui.activity.main.my.business;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.JsonUtil;
import com.yjy998.R;
import com.yjy998.common.adapter.HoldingsAdapter;
import com.yjy998.common.entity.ContractDetail;
import com.yjy998.common.entity.Holding;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.pop.CenterPopup;

import org.json.JSONArray;
import org.json.JSONException;

import static com.yjy998.ui.activity.main.my.business.capital.BuySellFragment.ContractObserver;

/**
 * 持仓
 */
public class HoldingsFragment extends BusinessListFragment {


    @Override
    public String getTitle() {
        return ContextUtil.getString(R.string.holdings);
    }
    Holding sel;
    @Override
    protected void onCreatePop(CenterPopup.PopWidget popWidget) {
        popWidget.add(new CenterPopup.PopItem(0, getString(R.string.buyIn), getResources().getColor(R.color.roundButtonBlue)));
        popWidget.add(new CenterPopup.PopItem(1, getString(R.string.sellOut), getResources().getColor(R.color.roundButtonRed)));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getHoldings();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemLongClick(parent, view, position, id);

        return true;
    }

    @Override
    protected void onPopItemClick(CenterPopup.PopItem item) {
        switch (item.id) {
            case 0: {

                break;
            }
            case 1: {
                break;
            }
        }
    }

    public void getHoldings() {
        if (getActivity() instanceof ContractObserver) {
            ContractDetail contract = ((ContractObserver) getActivity()).getContract();
            if (contract == null) {
                //没有选择合约
                return;
            }

            String contract_no = contract.contractId;
            SRequest request = new SRequest("http://www.yjy998.com/stock/hold");
            request.put("contract_no", contract_no);
            YHttpClient.getInstance().get(getActivity(), request, new YHttpHandler(false) {
                @Override
                protected void onStatusCorrect(Response response) {
                    try {
                        JSONArray array = new JSONArray(response.data);
                        HoldingsAdapter adapter = new HoldingsAdapter(getActivity(), JsonUtil.getArray(array, Holding.class));
                        setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    @Override
    public void refresh() {
        getHoldings();
    }
}
