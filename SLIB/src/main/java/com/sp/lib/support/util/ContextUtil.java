package com.sp.lib.support.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sp.lib.R;
import com.sp.lib.Slib;

import java.util.Random;

/**
 * 包装一个全局的Context对象,对Context一些常用的方法进行封装
 */
@SuppressWarnings("unused")
public class ContextUtil {
    private static Context context;

    private static int mToastLayoutId;

    public static final void init(Context context) {
        ContextUtil.context = context;
    }

    /**
     * @param toastLayout 用于toast的布局,里面要包含一个id为R.id.slib_toast_text_1的TextView
     */
    public static final void setToastLayout(int toastLayout) {
        mToastLayoutId = toastLayout;
    }

    public static final String getString(int id) {
        return context.getString(id);
    }

    public static int getVersion() {
        try {
            return getPackageInfo().versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static PackageInfo getPackageInfo() {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final void toast(Object o) {
        if (null == o)
            return;
        //要toast的信息
        String msg = String.valueOf(o);

        Toast toast;
        if (mToastLayoutId != 0) {
            toast = new Toast(context);
            View v = View.inflate(context, mToastLayoutId, null);
            toast.setView(v);
            toast.setGravity(Gravity.CENTER, 0, 0);
            TextView tv = (TextView) v.findViewById(R.id.slib_toast_text_1);
            if (tv != null) {
                tv.setText(msg);
            }
            toast.setDuration(Toast.LENGTH_SHORT);

        } else {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static final void toast(int resId) {
        toast(getString(resId));
    }

    public static final void toast_debug(Object o) {
        if (Slib.DEBUG) {
            toast(o);
        }
    }

    public static final Context getContext() {
        return context;
    }


    public static String getUUID() {
        String MIEI = "miei";

        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String miei = manager.getDeviceId();

        //如果为空就从SharedPreferences中取读
        if (miei == null) {
            miei = context.getSharedPreferences(MIEI, Context.MODE_PRIVATE).getString(MIEI, null);
        } else if (miei.length() == 14) {
            miei += "1";
        }

        //如果还是为空，就随机生成一个，并保存到SharedPreferences
        if (miei == null) {
            Random random = new Random();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 15; i++) {
                sb.append(random.nextInt(10));
            }
            miei = sb.toString();
            context.getSharedPreferences(MIEI, Context.MODE_PRIVATE).edit().putString(MIEI, miei);
        }

        return miei;
    }
}
