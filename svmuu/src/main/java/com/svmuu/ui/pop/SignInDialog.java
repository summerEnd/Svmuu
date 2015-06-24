package com.svmuu.ui.pop;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.svmuu.R;

public class SignInDialog extends Dialog{
    private EditText editname;
    private EditText editpsw;
    private CheckBox savepsw;
    private Button login;

    public SignInDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_sign_in);
    }

    private void initialize() {

        editname = (EditText) findViewById(R.id.edit_name);
        editpsw = (EditText) findViewById(R.id.edit_psw);
        savepsw = (CheckBox) findViewById(R.id.save_psw);
        login = (Button) findViewById(R.id.login);
    }
}
