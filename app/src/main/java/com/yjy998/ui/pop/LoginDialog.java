package com.yjy998.ui.pop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sp.lib.common.support.net.client.SRequest;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.account.User;
import com.yjy998.http.Response;
import com.yjy998.http.YHttpClient;
import com.yjy998.http.YHttpHandler;

import org.apache.http.Header;
import org.json.JSONObject;

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
                confirmButton.setText(R.string.login_ing);
                SRequest request = new SRequest();
                request.setUrl("http://www.yjy998.com/account/login");
                request.put("login_name", phoneEdit.getText().toString());
                request.put("login_passwd", passwordEdit.getText().toString());
                request.put("login_rsapwd", passwordEdit.getText().toString());

                YHttpClient.getInstance().post(getContext(), request, new YHttpHandler() {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        confirmButton.setText(R.string.re_login);
                    }

                    @Override
                    protected void onStatusCorrect(Response response) {
                        User user = AppDelegate.getInstance().getUser();
                        user.id = 0;
                        dismiss();
                    }
                });


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
        confirmButton.setOnClickListener(this);
    }
}
