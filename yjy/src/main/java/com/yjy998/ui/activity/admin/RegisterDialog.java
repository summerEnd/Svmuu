package com.yjy998.ui.activity.admin;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sp.lib.common.admin.AdminManager;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.time.TimeTicker;
import com.yjy998.R;
import com.yjy998.common.Constant;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.common.util.RSAUtil;
import com.yjy998.ui.pop.YAlertDialog;

public class RegisterDialog extends Dialog implements View.OnClickListener, RSAUtil.Callback {

    private EditText phoneEdit;
    private EditText passwordEdit;
    private EditText passwordRepeat;
    private EditText yzmEdit;
    private TextView resendText;
    private TextView protocolText;
    private Button confirmButton;
    TimeTicker countDownTime;
    private Context context;
    private AdminManager adminManager;
    private AdminManager codeCheck;

    public RegisterDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_register);
        initialize();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmButton: {

                if (adminManager == null) {
                    adminManager = new AdminManager();
                    adminManager
                            .addEmptyCheck(phoneEdit)
                            .addPatterCheck(phoneEdit, Constant.PATTERN_PHONE, context.getString(R.string.phone_not_correct))
                            .addEmptyCheck(passwordEdit)
                            .addLengthCheck(passwordEdit, 6, 20, context.getString(R.string.password_length_error))
                            .addEmptyCheck(passwordRepeat)
                            .addEqualCheck(passwordRepeat, passwordEdit, context.getString(R.string.repeat_not_equal))
                            .addEmptyCheck(yzmEdit)
                    ;
                }
                if (adminManager.start()) {
                    RSAUtil.sign(context, passwordEdit.getText().toString(), this);
                }

                break;
            }
            case R.id.closeButton: {
                dismiss();
                break;
            }
            case R.id.resendText: {
                if (codeCheck == null) {
                    codeCheck = new AdminManager();
                    codeCheck.addEmptyCheck(phoneEdit)
                            .addPatterCheck(phoneEdit, Constant.PATTERN_PHONE, context.getString(R.string.phone_not_correct));
                }
                if (codeCheck.start()) {
                    getCode();
                }
                break;
            }
        }
    }

    private void initialize() {
        phoneEdit = (EditText) findViewById(R.id.phoneEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        passwordRepeat = (EditText) findViewById(R.id.passwordRepeat);
        yzmEdit = (EditText) findViewById(R.id.yzmEdit);
        resendText = (TextView) findViewById(R.id.resendText);
        protocolText = (TextView) findViewById(R.id.protocolText);
        confirmButton = (Button) findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(this);
        resendText.setOnClickListener(RegisterDialog.this);

        findViewById(R.id.closeButton).setOnClickListener(this);
        protocolText.setText(getProtocol());
        countDownTime = new CodeCountDown(60);
    }

    /**
     * 用户协议
     */
    SpannableString getProtocol() {
        String string = getContext().getResources().getString(R.string.protocolText);
        SpannableString spannable = new SpannableString(string);
        spannable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

            }
        }, string.indexOf("《"), string.length(), 0);
        return spannable;
    }

    /**
     * 获取短信验证码
     */
    void getCode() {

        YHttpClient.getInstance().getCode(getContext(), phoneEdit.getText().toString(), new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                resendText.setClickable(false);
                countDownTime.start();
            }
        });
    }

    @Override
    public void onRSAEncodeSuccess(String rsa) {
        SRequest request = new SRequest("http://www.yjy998.com/account/register");
        request.put("pnumber", phoneEdit.getText().toString());
        request.put("password", passwordEdit.getText().toString());
        request.put("rsapassword", rsa);
        request.put("repassword", passwordRepeat.getText().toString());
        request.put("is_agree", true);
        confirmButton.setText(R.string.register_ing);
        YHttpClient.getInstance().post(getContext(), request, new YHttpHandler() {

            @Override
            protected void onStatusCorrect(Response response) {
                dismiss();
            }

            @Override
            protected void onStatusFailed(Response response) {
                YAlertDialog.show(context, context.getString(R.string.register_fail), response.message);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                confirmButton.setText(R.string.re_register);
            }
        });
    }

    @Override
    public void onRSAEncodeFailed() {

    }

    /**
     * 倒计时
     */
    private class CodeCountDown extends TimeTicker {

        protected CodeCountDown(int maxTime) {
            super(maxTime);
        }

        @Override
        public void onTick(int timeRemain) {
            resendText.setText(getContext().getString(R.string.resend_d, timeRemain));
        }

        @Override
        protected void onFinish() {
            resendText.setClickable(true);
            resendText.setText(R.string.resend);
        }
    }
}
