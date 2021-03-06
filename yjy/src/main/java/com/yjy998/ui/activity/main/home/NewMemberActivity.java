package com.yjy998.ui.activity.main.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.sp.lib.common.support.net.client.SRequest;
import com.yjy998.R;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.base.SecondActivity;
import com.yjy998.ui.activity.pay.PayDialog;
import com.yjy998.ui.pop.YAlertDialog;

public class NewMemberActivity extends SecondActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresh);
        findViewById(R.id.expNow).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expNow: {

                if (showLoginDialogIfNeed()) {
                    break;
                }
                final String price = "100";
                final PayDialog payDialog = new PayDialog(this, price);
                payDialog.setCallback(new PayDialog.Callback() {
                    @Override
                    public void onPay(String password, String rsa_password) {
                        SRequest request = new SRequest("http://www.yjy998.com/contract/apply");
                        request.put("apply_type", "TN");//TN或T9
                        request.put("deposit_amount", price);//总金额
                        request.put("pay_pwd", password);//支付密码
                        request.put("prev_store", 1);
                        request.put("pro_id", 1);
                        request.put("pro_term", "2");
                        request.put("trade_pwd", rsa_password);//交易密码

                        YHttpClient.getInstance().post(request, new YHttpHandler() {
                            @Override
                            protected void onStatusCorrect(Response response) {
                            }

                            @Override
                            protected void onStatusFailed(Response response) {
                                if (!TextUtils.isEmpty(response.message)){
                                    YAlertDialog.show(NewMemberActivity.this,response.message);
                                }
                            }
                        });
                    }
                }).show();
                break;
            }
        }
    }
}
