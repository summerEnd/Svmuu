package com.yjy998.ui.pop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sp.lib.common.admin.RSA;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.yjy998.AppDelegate;
import com.yjy998.BuildConfig;
import com.yjy998.R;
import com.yjy998.account.User;
import com.yjy998.http.Response;
import com.yjy998.http.YHttpClient;
import com.yjy998.http.YHttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginDialog extends Dialog implements View.OnClickListener {
    private EditText phoneEdit;
    private EditText passwordEdit;
    private Button confirmButton;
    private Context context;

    public LoginDialog(Context context) {
        super(context);
        this.context = context;
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
                confirmButton.requestFocus();
                //获取Rsa
                YHttpClient.getInstance().getRsa(new YHttpHandler() {
                    @Override
                    protected void onStatusCorrect(Response response) {
                        try {
                            JSONObject data = new JSONObject(response.data);
                            String password = passwordEdit.getText().toString();
                            String rsaPsw = RSA.encrypt(password, data.getString("modulus"), data.getString("exponent"));
                            String phone = phoneEdit.getText().toString();

                            SRequest request = new SRequest();
                            request.put("login_name", phone);
                            request.put("login_passwd", password);
                            request.put("login_rsapwd", rsaPsw);

                            //登录
                            YHttpClient.getInstance().login(getContext(), request, new YHttpHandler() {
                                @Override
                                public void onFinish() {
                                    super.onFinish();
                                    confirmButton.setText(R.string.re_login);
                                }

                                @Override
                                protected void onStatusFailed(Response response) {
                                    YAlertDialog.show(context, context.getString(R.string.login_fail), response.message);
                                }

                                @Override
                                protected void onStatusCorrect(Response response) {
                                    getUserInfo();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });


                break;
            }
            case R.id.closeButton: {
                dismiss();
                break;
            }
            case R.id.forgetText: {
                new ForgetPasswordDialog(context).show();
                break;
            }
        }
    }

    void getUserInfo() {
        confirmButton.setText(R.string.re_login);
        SRequest request = new SRequest();

        YHttpClient.getInstance().getByMethod(getContext(), "/h5/account/assentinfo", request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                AppDelegate.getInstance().setUser(JsonUtil.get(response.data, User.class));
                AppDelegate.getInstance().setLogined(true);
                dismiss();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                confirmButton.setText(R.string.re_login);
            }
        });
    }

    private void initialize() {

        phoneEdit = (EditText) findViewById(R.id.phoneEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        findViewById(R.id.forgetText).setOnClickListener(this);
        findViewById(R.id.closeButton).setOnClickListener(this);
        confirmButton.setOnClickListener(this);
        //todo 注释掉，此处方便调试
        if (BuildConfig.DEBUG) {
            phoneEdit.setText("13364388718");
            passwordEdit.setText("ldp8718");
        }

    }
}
