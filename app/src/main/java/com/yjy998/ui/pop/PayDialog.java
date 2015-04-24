package com.yjy998.ui.pop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sp.lib.widget.input.PasswordEdit;
import com.yjy998.R;

public class PayDialog extends Dialog implements View.OnClickListener {
    private TextView accountText;
    private TextView moneyText;
    private TextView remainMoneyText;
    private PasswordEdit passwordEdit;

    public PayDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay);
        initialize();
    }

    private void initialize() {

        accountText = (TextView) findViewById(R.id.accountText);
        moneyText = (TextView) findViewById(R.id.moneyText);
        remainMoneyText = (TextView) findViewById(R.id.remainMoneyText);
        passwordEdit = (PasswordEdit) findViewById(R.id.passwordEdit);
        findViewById(R.id.cancelBtn).setOnClickListener(this);
        findViewById(R.id.confirmBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmBtn: {
                break;
            }
            case R.id.cancelBtn: {
                dismiss();
                break;
            }
        }
    }
}
