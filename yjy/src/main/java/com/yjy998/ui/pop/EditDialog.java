package com.yjy998.ui.pop;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sp.lib.common.support.net.client.SRequest;
import com.yjy998.R;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;

public class EditDialog extends Dialog implements View.OnClickListener {

    protected EditText editPrice;
    protected TextView title;

    public EditDialog(Context context) {
        super(context);
        setContentView(R.layout.append_capital);
        initialize();
    }

    protected void initialize() {

        editPrice = (EditText) findViewById(R.id.editPrice);
        title = (TextView) findViewById(R.id.title);
        findViewById(R.id.buttonYes).setOnClickListener(this);
        findViewById(R.id.buttonNo).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonYes:
                onYes();
                break;
            case R.id.buttonNo:
                dismiss();
                break;
        }
    }

    protected void onYes(){

    }
}
