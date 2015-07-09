package com.sp.lib.common.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class DisplayUtil {

    public static DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }


    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }


    public static float dp(float value, Resources res) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, res.getDisplayMetrics());
    }

    public static String getScreenParams(Context context){
        DisplayMetrics dm=getDisplayMetrics(context);

        ;
        return dm.toString();
    }
}
