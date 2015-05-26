package com.yjy998.ui.pop;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.yjy998.R;

public class YProgressDialog extends Dialog {
    TextView msgText;
    public YProgressDialog(Context context) {
        super(context, android.R.style.Theme_Holo_Dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.y_progress_dialog);
        msgText= (TextView) findViewById(R.id.msgText);
    }

    public void setMessage(CharSequence message) {
        msgText.setText(message);
    }
}
