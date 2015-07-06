package com.svmuu.ui.pop;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sp.lib.common.util.ContextUtil;
import com.svmuu.R;


public class YAlertDialog extends Dialog implements View.OnClickListener {
    private TextView title;
    private TextView message;
    private TextView button;

    public YAlertDialog(Context context) {
        super(context);
        setContentView(R.layout.alert_dialog);
        initialize();
    }

    public static YAlertDialog showNoSuchFunction(Context context) {
        final YAlertDialog dialog = new YAlertDialog(context);
        dialog.setTitle(R.string.warn);
        dialog.setMessage(ContextUtil.getString(R.string.function_not_open));
        dialog.setButton(context.getString(R.string.yes), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }

    public static YAlertDialog show(Context context, String title, String message) {
        YAlertDialog dialog = new YAlertDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
        return dialog;
    }

    public static YAlertDialog show(Context context, int title, int message) {
        return show(context, context.getString(title), context.getString(message));
    }

    public static YAlertDialog show(Context context, String message) {
        YAlertDialog dialog = new YAlertDialog(context);
        dialog.setMessage(message);
        dialog.show();
        return dialog;
    }

    private void initialize() {

        title = (TextView) findViewById(R.id.title);
        message = (TextView) findViewById(R.id.message);
        button = (TextView) findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public void setButton(String button, View.OnClickListener listener) {
        this.button.setText(button);
        if (listener != null) {
            this.button.setOnClickListener(listener);
        } else {
            this.button.setOnClickListener(this);
        }
    }
}
