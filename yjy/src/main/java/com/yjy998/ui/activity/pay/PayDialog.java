package com.yjy998.ui.activity.pay;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.common.entity.Assent;
import com.yjy998.common.entity.User;
import com.yjy998.common.entity.UserInfo;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.common.util.RSAUtil;
import com.yjy998.ui.pop.YProgressDialog;

public class PayDialog extends Dialog implements View.OnClickListener, RSAUtil.Callback {
    private TextView accountText;
    private TextView moneyText;
    private TextView remainMoneyText;
    private String price;
    private EditText passwordEdit;
    Callback callback;
    View cancel;
    private Context context;

    public PayDialog(Context context, String price) {
        super(context);
        if (null == price) {
            price = "";
        }
        this.context = context;
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
        cancel = findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(this);
        findViewById(R.id.confirmBtn).setOnClickListener(this);
        refresh();
        getUserInfo();
    }

    void refresh() {
        Assent assent = AppDelegate.getInstance().getUser().assent;
        UserInfo userInfo = AppDelegate.getInstance().getUser().userInfo;
        accountText.setText(userInfo.phone);

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

                if (check()) {
                    cancel.setEnabled(false);
                    RSAUtil.sign(getContext(), passwordEdit.getText().toString(), this);
                }

                break;
            }
            case R.id.cancelBtn: {
                dismiss();
                break;
            }
        }
    }

    private boolean check() {
        if (TextUtils.isEmpty(passwordEdit.getText().toString())) {
            passwordEdit.requestFocus();
            passwordEdit.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
            return false;
        }

        return true;
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
        dismiss();
        if (callback != null) {
            callback.onPay(passwordEdit.getText().toString(), rsa);
        }
    }

    @Override
    public void onRSAEncodeFailed() {

        cancel.setEnabled(true);
    }

    public interface Callback {
        public void onPay(String password, String rsa_password);
    }

    void getUserInfo() {
        SRequest request = new SRequest();

        YHttpClient.getInstance().getByMethod(getContext(), "/h5/account/assentinfo", request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                AppDelegate.getInstance().setUser(JsonUtil.get(response.data, User.class));
                refresh();
            }

            @Override
            public Dialog onCreateDialog() {
                YProgressDialog dialog = new YProgressDialog(context);
                dialog.setMessage(getContext().getString(R.string.get_info));
                return dialog;
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

}
