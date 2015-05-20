package com.yjy998.ui.pop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sp.lib.widget.input.PasswordEdit;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.common.entity.Assent;
import com.yjy998.common.util.RSAUtil;

public class PayDialog extends Dialog implements View.OnClickListener, RSAUtil.Callback {
    private TextView accountText;
    private TextView moneyText;
    private TextView remainMoneyText;
    private TextView confirmBtn;
    private String price;
    private EditText passwordEdit;
    Callback callback;
    View cancel;

    public PayDialog(Context context, String price) {
        super(context);
        if (null == price) {
            price = "";
        }
        this.price = price;

        setContentView(R.layout.dialog_pay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
    }

    private void initialize() {

        accountText = (TextView) findViewById(R.id.accountText);
        moneyText = (TextView) findViewById(R.id.moneyText);
        remainMoneyText = (TextView) findViewById(R.id.remainMoneyText);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        confirmBtn = (TextView) findViewById(R.id.confirmBtn);
        cancel = findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        Assent assent = AppDelegate.getInstance().getUser().assent;

        accountText.setText(assent.name);

        moneyText.setText(getContext().getString(R.string.s_yuan, price));
        if (assent == null) {
            remainMoneyText.setText(getContext().getString(R.string.usable_s_money, "0"));
        } else {
            remainMoneyText.setText(getContext().getString(R.string.usable_s_money, assent.avalaible_amount));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmBtn: {
                cancel.setEnabled(false);
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

        cancel.setEnabled(true);
        confirmBtn.setText(R.string.retry);
    }

    public interface Callback {
        public void onPay(String password, String rsa_password);
    }
}
