package com.yjy998.ui.pop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sp.lib.common.util.SLog;
import com.sp.lib.common.util.time.TimeTicker;
import com.yjy998.R;

public class RegisterDialog extends Dialog implements View.OnClickListener {

    private EditText phoneEdit;
    private EditText passwordEdit;
    private EditText yzmEdit;
    private TextView resendText;
    private TextView protocolText;
    private Button confirmButton;
    TimeTicker countDownTime;

    public RegisterDialog(Context context) {
        super(context);
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

                break;
            }
            case R.id.closeButton: {
                dismiss();
                break;
            }
            case R.id.resendText: {
                resendText.setClickable(false);
                countDownTime.start();
                break;
            }
        }
    }

    private void initialize() {
        phoneEdit = (EditText) findViewById(R.id.phoneEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        yzmEdit = (EditText) findViewById(R.id.yzmEdit);
        resendText = (TextView) findViewById(R.id.resendText);
        protocolText = (TextView) findViewById(R.id.protocolText);
        confirmButton = (Button) findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(this);
        resendText.setOnClickListener(RegisterDialog.this);

        String string = getContext().getResources().getString(R.string.protocolText);
        SpannableString spannable = new SpannableString(string);
        spannable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

            }
        }, string.indexOf("《"), string.length(), 0);

        findViewById(R.id.closeButton).setOnClickListener(this);
        protocolText.setText(spannable);
        countDownTime = new TimeTicker(10) {

            @Override
            public void onTick(int time) {
                SLog.debug("--->" + time);
                resendText.setText(getContext().getString(R.string.resend_d, time));
            }

            @Override
            public void onFinish() {
                resendText.setClickable(true);
                resendText.setText(R.string.resend);
            }
        };
        resendText.setClickable(false);
        countDownTime.start();
    }
}
