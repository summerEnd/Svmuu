package com.svmuu.ui.pop;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.svmuu.R;


public class YAlertDialogTwoButton extends Dialog implements View.OnClickListener {
    private TextView title;
    private TextView message;
    private TextView button1;
    private TextView button2;

    public YAlertDialogTwoButton(Context context) {
        super(context);
        setContentView(R.layout.alert_dialog_2_button);
        initialize();
    }

    public static YAlertDialogTwoButton show(Context context, String title, String message) {
        YAlertDialogTwoButton dialog = new YAlertDialogTwoButton(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
        return dialog;
    }

    public static YAlertDialogTwoButton show(Context context, int title, int message) {
        return show(context, context.getString(title), context.getString(message));
    }

    private void initialize() {

        title = (TextView) findViewById(R.id.title);
        message = (TextView) findViewById(R.id.message);
        button1 = (TextView) findViewById(R.id.button1);
        button2 = (TextView) findViewById(R.id.button2);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        OnClickListener listener = (OnClickListener) v.getTag(id);
        if (listener != null) {
            listener.onClick(this, id);
        }
        dismiss();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public YAlertDialogTwoButton setButton1(int res, OnClickListener listener) {
        return setButton1(getContext().getResources().getString(res), listener);
    }

    public YAlertDialogTwoButton setButton1(String button, OnClickListener listener) {
        this.button1.setText(button);
        this.button1.setTag(R.id.button1, listener);
        return this;
    }

    public YAlertDialogTwoButton setButton2(int res, OnClickListener listener) {
        return setButton2(getContext().getResources().getString(res), listener);
    }

    public YAlertDialogTwoButton setButton2(String button, OnClickListener listener) {
        this.button2.setText(button);
        this.button1.setTag(R.id.button2, listener);
        return this;
    }


}
