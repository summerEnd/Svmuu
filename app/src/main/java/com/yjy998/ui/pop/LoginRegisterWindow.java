package com.yjy998.ui.pop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.yjy998.R;

import static android.view.View.*;

@SuppressLint("InflateParams")
public class LoginRegisterWindow extends PopupWindow implements OnClickListener {
    View layout;
    RegisterDialog mRegisterDialog;
    LoginDialog mLoginDialog;

    public LoginRegisterWindow(Context context) {
        super(context);
        layout = LayoutInflater.from(context).inflate(R.layout.window_register_login, null);
        setContentView(layout);
        initialize();
    }

    private void initialize() {
        setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        findViewById(R.id.loginButton).setOnClickListener(this);
        findViewById(R.id.registerButton).setOnClickListener(this);

        setBackgroundDrawable(new ColorDrawable(0));

    }

    View findViewById(int id) {
        return layout.findViewById(id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton: {
                if (mLoginDialog == null) {
                    mLoginDialog = new LoginDialog(v.getContext());
                }
                mLoginDialog.show();
                break;
            }
            case R.id.registerButton: {
                if (mRegisterDialog == null) {
                    mRegisterDialog = new RegisterDialog(v.getContext());
                }
                mRegisterDialog.show();
                break;
            }
        }
    }
}
