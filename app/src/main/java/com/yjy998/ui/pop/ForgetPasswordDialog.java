package com.yjy998.ui.pop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.common.util.time.TimeTicker;
import com.yjy998.AppDelegate;
import com.yjy998.BuildConfig;
import com.yjy998.R;
import com.yjy998.account.User;
import com.yjy998.http.Response;
import com.yjy998.http.YHttpClient;
import com.yjy998.http.YHttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPasswordDialog extends Dialog implements View.OnClickListener {


    private EditText phoneEdit;
    private EditText yzmEdit;
    private TextView resendText;
    private EditText passwordEdit;
    private Button confirmButton;
    TimeTicker countDownTime;

    public ForgetPasswordDialog(Context context) {
        super(context);
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
                confirmButton.setText(R.string.login_ing);
                confirmButton.requestFocus();
                //获取Rsa
                YHttpClient.getInstance().getRsa(new YHttpHandler() {
                    @Override
                    protected void onStatusCorrect(Response response) {
                        try {

                            JSONObject data = new JSONObject(response.data);
                            SRequest request = new SRequest();
                            request.setUrl("http://www.yjy998.com/account/resetloginpwd");
                            request.put("phone", phoneEdit.getText().toString());
                            request.put("login_pwd", passwordEdit.getText().toString());
                            request.put("new_psw_rsa", "73c3deb2f89d08647d6311ac513a2538829ed329c1ac8413e0074ea735d0eeabffd17f08acd8bce9431802bde2e2614a0073d95fc1d80bdceb5ca9c053ccf472846acea88dab76f5c529b1efd4b4fa5e33a88e2c52c6a1cf4acf2dd1908f2850f0db502a7fe9082a60c80ee0c314a04864db55cce11900976ff293965ea7af13");
                            request.put("re_login_pwd", passwordEdit.getText().toString());
                            request.put("biz_sms", yzmEdit.getText().toString());

                            YHttpClient.getInstance().post(getContext(), request, new YHttpHandler() {
                                @Override
                                protected void onStatusCorrect(Response response) {

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

            case R.id.resendText:{
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
