package com.yjy998.ui.pop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.yjy998.R;

public class LoginDialog extends Dialog implements View.OnClickListener {
    private EditText phoneEdit;
    private EditText passwordEdit;
    private Button confirmButton;

    public LoginDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);
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
            case R.id.forgetText: {

                break;
            }
        }
    }

    private void initialize() {

        phoneEdit = (EditText) findViewById(R.id.phoneEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        findViewById(R.id.forgetText).setOnClickListener(this);
        findViewById(R.id.closeButton).setOnClickListener(this);
    }
}
