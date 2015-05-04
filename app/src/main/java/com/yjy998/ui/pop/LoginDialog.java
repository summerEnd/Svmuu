package com.yjy998.ui.pop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.JsonUtil;
import com.yjy998.AppDelegate;
import com.yjy998.BuildConfig;
import com.yjy998.R;
import com.yjy998.account.User;
import com.yjy998.common.preference.CookiePreference;
import com.yjy998.http.Response;
import com.yjy998.http.YHttpClient;
import com.yjy998.http.YHttpHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.interfaces.RSAKey;

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
                confirmButton.requestFocus();
                //获取Rsa
                YHttpClient.getInstance().getRsa(new YHttpHandler() {
                    @Override
                    protected void onStatusCorrect(Response response) {
                        try {
                            JSONObject data = new JSONObject(response.data);
                            SRequest request = new SRequest();
                            request.put("login_name", phoneEdit.getText().toString());
                            request.put("login_passwd", passwordEdit.getText().toString());
                            request.put("login_rsapwd", "73c3deb2f89d08647d6311ac513a2538829ed329c1ac8413e0074ea735d0eeabffd17f08acd8bce9431802bde2e2614a0073d95fc1d80bdceb5ca9c053ccf472846acea88dab76f5c529b1efd4b4fa5e33a88e2c52c6a1cf4acf2dd1908f2850f0db502a7fe9082a60c80ee0c314a04864db55cce11900976ff293965ea7af13");
                            //登录
                            YHttpClient.getInstance().login(getContext(), request, new YHttpHandler() {
                                @Override
                                public void onFinish() {
                                    super.onFinish();
                                    confirmButton.setText(R.string.re_login);
                                }

                                @Override
                                protected void onStatusCorrect(Response response) {
                                    AppDelegate.getInstance().setLogined(true);
                                    getUserInfo();
                                }
                            });
                        } catch (JSONException e) {
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

                break;
            }
        }
    }

    void getUserInfo() {
        confirmButton.setText(R.string.re_login);
        SRequest request = new SRequest();
        String cookie = CookiePreference.get().getCookie();
        if (!TextUtils.isEmpty(cookie)) {
//            ContextUtil.toast_debug("add cookie:" + cookie);
//            YHttpClient.getInstance().getClient().addHeader(CookiePreference.COOKIE, cookie);
        }
        YHttpClient.getInstance().getByMethod(getContext(), "/h5/account/assentinfo", request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                AppDelegate.getInstance().setUser(JsonUtil.get(response.data, User.class));
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
