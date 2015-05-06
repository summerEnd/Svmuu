package com.yjy998.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.sp.lib.common.support.net.client.SRequest;
import com.yjy998.R;
import com.yjy998.entity.Contract;
import com.yjy998.http.Response;
import com.yjy998.http.YHttpClient;
import com.yjy998.http.YHttpHandler;

public class ContractInfo extends Activity {

    public static final String EXTRA_CONTRACT_NO = "CONTRACT_NO";
    private TextView usableText;
    private TextView balanceText;
    private TextView stockValueText;
    private TextView totalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_info);
        getContractInfo();
        initialize();
    }

    void getContractInfo() {
        SRequest request = new SRequest();
        request.put("contract_no", getIntent().getStringExtra(EXTRA_CONTRACT_NO));
        YHttpClient.getInstance().getByMethod(this, "/h5/account/contractinfo", request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {

            }
        });
    }

    private void initialize() {

        usableText = (TextView) findViewById(R.id.usableText);
        balanceText = (TextView) findViewById(R.id.balanceText);
        stockValueText = (TextView) findViewById(R.id.stockValueText);
        totalText = (TextView) findViewById(R.id.totalText);

        Contract contract= (Contract) getIntent().getSerializableExtra("bean");
        if (contract!=null){
            usableText.setText(contract.cash_amount);
//            balanceText.setText(contract.);
        }
    }
}
