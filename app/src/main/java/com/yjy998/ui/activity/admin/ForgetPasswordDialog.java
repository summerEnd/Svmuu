package com.yjy998.ui.activity.admin;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

public class ForgetPasswordDialog extends Dialog implements View.OnClickListener, RSAUtil.Callback {


    private EditText phoneEdit;
    private EditText yzmEdit;
    private TextView resendText;
    private EditText passwordEdit;
    private Button confirmButton;
    TimeTicker countDownTime;
    private Context context;
    AdminManager adminManager;
    public ForgetPasswordDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_forget);
        initialize();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmButton: {
                if (adminManager == null) {
                    adminManager = new AdminManager();
                    adminManager.addEmptyCheck(phoneEdit)
                            .addPatterCheck(phoneEdit, Constant.PATTERN_PHONE, context.getString(R.string.phone_not_correct))
                            .addEmptyCheck(passwordEdit)
                            .addLengthCheck(passwordEdit, 6, 20, context.getString(R.string.password_length_error))
                            .addEmptyCheck(yzmEdit)
                    ;
                }
                if (adminManager.start()){
                    confirmButton.setText(R.string.op_ing);
                    confirmButton.requestFocus();
                    RSAUtil.sign(getContext(), passwordEdit.getText().toString(), this);
                }

                break;
            }
            case R.id.closeButton: {
                dismiss();
                break;
            }

            case R.id.resendText: {
                getCode();
                break;
            }

        }
    }


    private void initialize() {
        yzmEdit = (EditText) findViewById(R.id.yzmEdit);
        resendText = (TextView) findViewById(R.id.resendText);

        phoneEdit = (EditText) findViewById(R.id.phoneEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        findViewById(R.id.closeButton).setOnClickListener(this);
        confirmButton.setOnClickListener(this);
        resendText.setOnClickListener(this);

        countDownTime = new CodeCountDown(60);
    }

    /**
     * 获取短信验证码
     */
    void getCode() {
        resendText.setClickable(false);
        countDownTime.start();
        YHttpClient.getInstance().getCode(getContext(), phoneEdit.getText().toString(), new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {

            }
        });
    }

    @Override
    public void onRSAEncodeSuccess(String rsa) {

        SRequest request = new SRequest();
        request.setUrl("http://www.yjy998.com/account/resetloginpwd");
        request.put("phone", phoneEdit.getText().toString());
        request.put("login_pwd", passwordEdit.getText().toString());
        request.put("new_psw_rsa", rsa);
        request.put("re_login_pwd", passwordEdit.getText().toString());
        request.put("biz_sms", yzmEdit.getText().toString());

        YHttpClient.getInstance().post(getContext(), request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                YAlertDialog.show(context, response.message).setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dismiss();
                    }
                });
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
