package com.yjy998.ui.pop;

import android.app.ProgressDialog;
import android.content.Context;

public class YProgressDialog extends ProgressDialog {
    public YProgressDialog(Context context) {
        super(context);
        setProgressStyle(STYLE_SPINNER);
    }
}
