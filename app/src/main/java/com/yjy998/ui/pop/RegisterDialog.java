package com.yjy998.ui.pop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.SLog;
import com.sp.lib.common.util.time.TimeTicker;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.account.User;
import com.yjy998.http.Response;
import com.yjy998.http.YHttpClient;
import com.yjy998.http.YHttpHandler;

public class RegisterDialog extends Dialog implements View.OnClickListener {

    private EditText phoneEdit;
    private EditText passwordEdit;
    private EditText passwordRepeat;
    private EditText yzmEdit;
    private TextView resendText;
    private TextView protocolText;
    private Button confirmButton;
    TimeTicker countDownTime;
    private Context context;
    public RegisterDialog(Context context) {
        super(context);
        this.context=context;
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
                SRequest request = new SRequest("http://www.yjy998.com/account/register");
                request.put("pnumber", phoneEdit.getText().toString());
                request.put("password", passwordEdit.getText().toString());
                request.put("repassword", passwordRepeat.getText().toString());
                request.put(",is_agree", true);
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
        resendText.setClickable(false);
        countDownTime.start();
        YHttpClient.getInstance().getCode(getContext(), phoneEdit.getText().toString(), new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {

            }
        });
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
