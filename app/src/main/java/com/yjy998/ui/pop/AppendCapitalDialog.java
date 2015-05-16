package com.yjy998.ui.pop;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.sp.lib.common.support.net.client.SRequest;
import com.yjy998.R;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;

public class AppendCapitalDialog extends Dialog implements View.OnClickListener {

    private EditText editPrice;
    private String contract_id;

    public AppendCapitalDialog(Context context, String contract_id) {
        super(context);
        this.contract_id = contract_id;
        setContentView(R.layout.append_capital);
        initialize();
    }

    private void initialize() {

        editPrice = (EditText) findViewById(R.id.editPrice);
        findViewById(R.id.buttonYes).setOnClickListener(this);
        findViewById(R.id.buttonNo).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonYes:
                addCapital();
                break;
            case R.id.buttonNo:
                dismiss();
                break;
        }
    }

    /**
     * 获取追加的资金数量
     *
     */
    public String getAppendAmount() {
        String s = editPrice.getText().toString();
        return s.isEmpty() ? "0" : s;
    }

    public void addCapital() {
        SRequest request = new SRequest("http://www.yjy998.com/asset/append");
        request.put("contract_no", contract_id);
        request.put("amount", getAppendAmount());
        YHttpClient.getInstance().post(getContext(), request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                dismiss();
            }
        });
    }
}
