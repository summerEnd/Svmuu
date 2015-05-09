package com.yjy998.ui.activity.main.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sp.lib.common.util.JsonUtil;
import com.yjy998.R;
import com.yjy998.common.entity.ContractDetail;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.main.my.business.BusinessActivity;
import com.yjy998.ui.activity.base.SecondActivity;
import com.yjy998.ui.pop.AppendCapitalDialog;
import com.yjy998.ui.pop.PayDialog;

public class ContractInfoActivity extends SecondActivity implements DialogInterface.OnDismissListener, PayDialog.Callback {

    public static final String EXTRA_CONTRACT_NO = "CONTRACT_NO";

    AppendCapitalDialog appendCapitalDialog;
    private TextView manageFeeText;
    private TextView positionText;
    private TextView balanceText;
    private TextView contractType;
    private TextView contractNo;
    private TextView rateText;
    private TextView applyDate;
    private TextView totalText;
    private TextView areaText;
    private ContractDetail detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_info);
        initialize();
    }

    void getContractInfo() {
        YHttpClient.getInstance().getContractInfo(this, getIntent().getStringExtra(EXTRA_CONTRACT_NO), new YHttpHandler(false) {
            @Override
            protected void onStatusCorrect(Response response) {
                try {
                    detail = JsonUtil.get(response.data, ContractDetail.class);
                    contractNo.setText(getString(R.string.contract_no_s, detail.contractId));
                    contractType.setText(getString(R.string.contractType_s, detail.contract_type));
                    totalText.setText(getString(R.string.total_capital_s, detail.totalAsset));
                    manageFeeText.setText(getString(R.string.s_day, detail.accountFee));
                    areaText.setText(getString(R.string.relative_area_s, detail.relatedContest));
                    applyDate.setText(getString(R.string.apply_date_s, detail.applyTime));
                    balanceText.setText(detail.totalProfit);
                    rateText.setText(detail.profitRatio + "%");
                    positionText.setText(detail.storageRatio);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {

        if (detail == null) {
            return;
        }

        switch (v.getId()) {
            case R.id.addCapital: {
                if (appendCapitalDialog == null) {
                    appendCapitalDialog = new AppendCapitalDialog(this, detail.contractId);
                    appendCapitalDialog.setOnDismissListener(this);
                }
                appendCapitalDialog.show();
                break;
            }
            case R.id.buyIn: {
                startActivity(new Intent(this, BusinessActivity.class)
                        .putExtra(BusinessActivity.EXTRA_IS_BUY, true));
                break;
            }
            case R.id.sellOut: {
                startActivity(new Intent(this, BusinessActivity.class)
                        .putExtra(BusinessActivity.EXTRA_IS_BUY, false));
                break;
            }
        }
    }

    private void initialize() {

        manageFeeText = (TextView) findViewById(R.id.manageFeeText);
        positionText = (TextView) findViewById(R.id.positionText);
        balanceText = (TextView) findViewById(R.id.balanceText);
        contractType = (TextView) findViewById(R.id.contractType);
        contractNo = (TextView) findViewById(R.id.contractNo);
        rateText = (TextView) findViewById(R.id.rateText);
        applyDate = (TextView) findViewById(R.id.applyDate);
        totalText = (TextView) findViewById(R.id.totalText);
        areaText = (TextView) findViewById(R.id.areaText);
        findViewById(R.id.buyIn).setOnClickListener(this);
        findViewById(R.id.sellOut).setOnClickListener(this);
        findViewById(R.id.addCapital).setOnClickListener(this);
        getContractInfo();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        appendCapitalDialog.getAppendAmount();
    }


    @Override
    public void onPay(String password, String rsa_password) {

    }
}
