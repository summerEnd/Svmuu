package com.yjy998.ui.pop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sp.lib.widget.input.PasswordEdit;
import com.yjy998.R;

public class PayDialog extends Dialog implements View.OnClickListener, RSAUtil.Callback {
    private TextView accountText;
    private TextView moneyText;
    private TextView remainMoneyText;
    private TextView confirmBtn;

    private PasswordEdit passwordEdit;
    Callback callback;

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
        confirmBtn = (TextView) findViewById(R.id.confirmBtn);
        findViewById(R.id.cancelBtn).setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmBtn: {
                confirmBtn.setText(R.string.dealing);
                RSAUtil.sign(getContext(), passwordEdit.getText().toString(), this);
                break;
            }
            case R.id.cancelBtn: {
                dismiss();
                break;
            }
        }
    }

    public Callback getCallback() {
        return callback;
    }

    public PayDialog setCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public void onRSAEncodeSuccess(String rsa) {
        if (callback != null) {
            callback.onPay(passwordEdit.getText().toString(), rsa);
            dismiss();
        }
    }

    @Override
    public void onRSAEncodeFailed() {
        confirmBtn.setText(R.string.retry);
    }

    public interface Callback {
        public void onPay(String password, String rsa_password);
    }
}
