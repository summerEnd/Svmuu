package com.yjy998.ui.activity.main.my.business;


import android.content.Intent;

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
import com.yjy998.ui.activity.main.my.business.capital.TradeFragment;
import com.yjy998.ui.pop.CenterPopup;

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
    EntrustAdapter adapter;
    private boolean jump = false;

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
    public void onPreparePop(CenterPopup popup) {
        PopWidget widget = popup.getPop();
        PopItem byId = widget.getById(2);
        int position = getSelectedPosition();
        Entrust entrust = entrusts.get(position);
        byId.color = getResources().getColor(R.color.roundButtonBlue);
        if ("1".equals(entrust.entrustDirection)) {
            byId.text=getString(R.string.cancelAndRebuy);
            byId.color=getResources().getColor(R.color.roundButtonRed);
        }else{
            byId.text=getString(R.string.cancelAndSell);
            byId.color=getResources().getColor(R.color.roundButtonBlue);
        }

    }

    @Override
    protected void onPopItemClick(PopItem item) {
        Entrust entrust = entrusts.get(getSelectedPosition());
        switch (item.id) {
            case 1: {
                jump = false;
                cancel(entrust);
                break;
            }
            case 2: {
                jump = true;
                cancel(entrust);
                break;
            }
            case 3: {

                break;
            }
        }
    }


    @Override
    public void refresh() {
        if (getActivity() instanceof TradeFragment.ContractObserver) {
            ContractDetail contract = ((TradeFragment.ContractObserver) getActivity()).getSharedContract();
            if (contract == null) {
                //没有选择合约
                return;
            }
            entrusts.clear();
            if (adapter == null) {
                adapter = new EntrustAdapter(getActivity(), entrusts);
                setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
            String contract_no = contract.contractId;
            SRequest request = new SRequest("http://www.yjy998.com/stock/entrust");
            request.put("contract_no", contract_no);
            YHttpClient.getInstance().get(getActivity(), request, new YHttpHandler(false) {
                @Override
                protected void onStatusCorrect(Response response) {
                    try {
                        JSONArray array = new JSONArray(response.data);
                        JsonUtil.getArray(array, Entrust.class, entrusts);

                        adapter.notifyDataSetChanged();

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
    public void cancel(final Entrust entrust) {
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
                if (jump) {
                    startActivity(new Intent(getActivity(), BusinessActivity.class)
                                    .putExtra(BusinessActivity.EXTRA_STOCK_CODE, entrust.stockCode)
                                    .putExtra(BusinessActivity.EXTRA_CONTRACT_NO, getSharedContract().contractId)
                            .putExtra(BusinessActivity.EXTRA_IS_BUY,"1".equals(entrust.entrustDirection))
                    );
                }
            }
        });
    }

}
