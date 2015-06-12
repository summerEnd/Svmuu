package com.yjy998.ui.pop;

import android.content.Context;

import com.sp.lib.common.support.net.client.SRequest;
import com.yjy998.R;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;

public class TokenDialog extends EditDialog{

    private boolean isTokenRight=false;

    public TokenDialog(Context context) {
        super(context);
    }

    @Override
    protected void initialize() {
        super.initialize();
        title.setText(R.string.apply_token);
    }

    @Override
    protected void onYes() {
        isTokenRight=false;//初始化
        SRequest request = new SRequest("http://mobile.yjy998.com/h5/contest/getelitcode");
        request.put("code", editPrice.getText().toString());
        YHttpClient.getInstance().post(getContext(), request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                isTokenRight=true;
                dismiss();
            }

        });
    }

    public boolean isTokenRight() {
        return isTokenRight;
    }
}
