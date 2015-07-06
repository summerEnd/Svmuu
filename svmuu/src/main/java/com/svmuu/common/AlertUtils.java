package com.svmuu.common;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.svmuu.R;

public class AlertUtils {
    /**
     * 温馨提示
     */
    public static AlertDialog warn(Context context,String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(R.string.warn);
        builder.setMessage(message);
        return builder.create();
    }

}
