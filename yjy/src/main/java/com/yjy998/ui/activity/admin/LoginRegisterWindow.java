package com.yjy998.ui.activity.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.ui.activity.admin.LoginDialog;
import com.yjy998.ui.activity.admin.RegisterDialog;

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
        findViewById(R.id.closeImage).setOnClickListener(this);

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
                    mLoginDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (AppDelegate.getInstance().isUserLogin()){
                                dismiss();
                            }
                        }
                    });
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
            case R.id.closeImage: {
                dismiss();
                break;
            }
        }
    }
}
