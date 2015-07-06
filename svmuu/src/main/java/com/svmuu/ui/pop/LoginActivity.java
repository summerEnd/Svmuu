package com.svmuu.ui.pop;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.BaseKeyListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.sp.lib.common.support.net.client.SRequest;
import com.svmuu.AppDelegate;
import com.svmuu.R;
import com.svmuu.common.config.Preference;
import com.svmuu.common.entity.User;
import com.svmuu.common.http.HttpHandler;
import com.svmuu.common.http.HttpManager;
import com.svmuu.common.http.Response;
import com.svmuu.ui.BaseActivity;

import org.apache.http.Header;

public class LoginActivity extends BaseActivity {
    private EditText editname;
    private EditText editpsw;
    private CheckBox savepsw;
    private Button login;
    private SharedPreferences sp_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sign_in);
        initialize();
    }

    private void initialize() {

        editname = (EditText) findViewById(R.id.edit_name);
        editpsw = (EditText) findViewById(R.id.edit_psw);
        savepsw = (CheckBox) findViewById(R.id.save_psw);


        sp_user = Preference.get(this, Preference.USER.class);
        boolean isSavePassword = sp_user.getBoolean(Preference.USER.IS_SAVE_PASSWORD, true);
        savepsw.setChecked(isSavePassword);
        editname.setText(sp_user.getString(Preference.USER.USER_NAME, ""));

        if (isSavePassword) {
            editpsw.setText(sp_user.getString(Preference.USER.USER_PASSWORD, ""));
        }
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SRequest request = new SRequest("login");
                final String userName = editname.getText().toString();
                final String password = editpsw.getText().toString();

                request.put("name", userName);
                request.put("pwd", password);


                HttpManager.getInstance().postMobileApi(null, request, new HttpHandler() {
                    @Override
                    public void onResultOk(int statusCOde, Header[] headers, Response response) {

                        SharedPreferences.Editor editor = sp_user.edit();

                        editor.putString(Preference.USER.USER_NAME, userName)
                                .putBoolean(Preference.USER.IS_SAVE_PASSWORD, savepsw.isChecked());

                        if (savepsw.isChecked()) {
                            editor.putString(Preference.USER.USER_PASSWORD, password);
                        } else {
                            editor.putString(Preference.USER.USER_PASSWORD, "");
                        }
                        editor.apply();

                        User user = AppDelegate.getInstance().getUser();
                        user.name = userName;
                        user.password = password;
                        finish();
                    }

                });
            }
        });
    }
}
