package com.yjy998.ui.pop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yjy998.R;

public class YDownloadDialog extends Dialog {
    private int max;
    private String msg;
    private TextView title;
    private ProgressBar progressBar;
    private TextView progressText;
    private TextView button;

    public YDownloadDialog(Context context) {
        super(context);
        setContentView(R.layout.progress_layout);
        initialize();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        if (progressBar != null) {
            progressBar.setMax(max);
        }
    }

    public void setProgress(int progress) {
        progressBar.setProgress(progress);
        int p = (int) (progress / (float) max * 100);
        progressText.setText(String.format("%d%%", p));
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private void initialize() {

        title = (TextView) findViewById(R.id.title);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressText = (TextView) findViewById(R.id.progressText);
        button = (TextView) findViewById(R.id.button);

        progressBar.setMax(max);
        setProgress(0);
    }

    public void setButton(String text, View.OnClickListener listener) {
        button.setOnClickListener(listener);
        button.setText(text);
    }
    public void setMessage(String msg){}
}
