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

public class AppendCapitalDialog extends EditDialog implements View.OnClickListener {

    private String contract_id;
    private Context context;

    public AppendCapitalDialog(Context context, String contract_id) {
        super(context);
        this.context=context;
        this.contract_id = contract_id;
    }

    @Override
    protected void initialize() {
        super.initialize();
        title.setText(R.string.append_amount);
    }

    @Override
    protected void onYes() {
        SRequest request = new SRequest("http://www.yjy998.com/asset/append");
        request.put("contract_no", contract_id);
        request.put("amount", getAppendAmount());
        YHttpClient.getInstance().post(getContext(), request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                dismiss();
            }

            @Override
            public Dialog onCreateDialog() {

                return new YProgressDialog(context);
            }
        });
    }

    /**
     * 获取追加的资金数量
     *
     */
    public String getAppendAmount() {
        String s = editPrice.getText().toString();
        return s.isEmpty() ? "0" : s;
    }
}
