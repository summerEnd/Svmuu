package com.yjy998.ui.activity.main.my.business;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.JsonUtil;
import com.yjy998.R;
import com.yjy998.common.adapter.EntrustAdapter;
import com.yjy998.common.entity.ContractDetail;
import com.yjy998.common.entity.Entrust;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.main.my.business.capital.BuySellFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.yjy998.ui.pop.CenterPopup.PopItem;
import static com.yjy998.ui.pop.CenterPopup.PopWidget;

/**
 * 委托/撤单
 */
public class CancellationEntrustFragment extends BusinessListFragment {

    private List<Entrust> entrusts = new ArrayList<Entrust>();

    @Override
    public String getTitle() {
        return ContextUtil.getString(R.string.cancellationEntrust);
    }

    @Override
    protected void onCreatePop(PopWidget popWidget) {
        popWidget.add(new PopItem(1, getString(R.string.cancelOrder), getResources().getColor(R.color.roundButtonBlue)));
        popWidget.add(new PopItem(2, getString(R.string.cancelAndRebuy), getResources().getColor(R.color.roundButtonRed)));
    }


    @Override
    protected void onPopItemClick(PopItem item) {
        switch (item.id) {
            case 1: {
                cancel(entrusts.get(getSelectedPosition()));
                break;
            }
            case 2: {
                break;
            }
        }
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
            SRequest request = new SRequest("http://www.yjy998.com/stock/entrust");
            request.put("contract_no", contract_no);
            YHttpClient.getInstance().get(getActivity(), request, new YHttpHandler(false) {
                @Override
                protected void onStatusCorrect(Response response) {
                    try {
                        JSONArray array = new JSONArray(response.data);
                        entrusts.clear();
                        JsonUtil.getArray(array, Entrust.class, entrusts);

                        EntrustAdapter adapter = (EntrustAdapter) getAdapter();
                        if (adapter == null) {
                            adapter = new EntrustAdapter(getActivity(), entrusts);
                            setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    /**
     * 参数：contract_id（合约编号），entrust_no（委托接口中的entrustNo），stock_code（股票代码），stock_name（股票名），withdrawal_amount（撤单数量，委托接口中的entrustAmount数量）
     */
    public void cancel(Entrust entrust) {
        ContractDetail detail = getSharedContract();
        if (detail == null) {
            ContextUtil.toast(R.string.contract_not_selected);
            return;
        }
        SRequest request = new SRequest("http://www.yjy998.com/stock/withdrawal");
        request.put("contract_id", detail.contractId);
        request.put("entrust_no", entrust.entrustNo);
        request.put("stock_code", entrust.stockCode);
        request.put("stock_name", entrust.stockName);
        request.put("withdrawal_amount", entrust.withdrawAmount);
        YHttpClient.getInstance().post(request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {

            }
        });
    }

}
