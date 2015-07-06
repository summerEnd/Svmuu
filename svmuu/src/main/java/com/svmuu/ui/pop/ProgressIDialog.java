package com.svmuu.ui.pop;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;
import android.widget.TextView;

import com.svmuu.R;


public class ProgressIDialog  extends ProgressDialog{

    public ProgressIDialog(Context context) {
        this(context,android.support.v7.appcompat.R.style.Theme_AppCompat_Light_Dialog);
    }

    public ProgressIDialog(Context context, int theme) {
        super(context, theme);
        getWindow().setBackgroundDrawable(new ColorDrawable());
    }

    public static ProgressIDialog show(Context context,String message){
        ProgressIDialog dialog=new ProgressIDialog(context);
        dialog.setMessage(message);
        dialog.show();
        return dialog;
    }
}
