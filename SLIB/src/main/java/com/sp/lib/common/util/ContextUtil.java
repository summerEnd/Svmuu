package com.sp.lib.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sp.lib.R;
import com.sp.lib.SApplication;

import java.util.Random;

/**
 * 包装一个全局的Context对象,对Context一些常用的方法进行封装
 */
@SuppressWarnings("unused")
public class ContextUtil {
    private static Context context;

    private static int mToastLayoutId;
    private static Toast mToast;
    private static int TOAST_GRAVITY = Gravity.NO_GRAVITY;

    public static void init(Context context) {
        ContextUtil.context = context;
    }

    /**
     * @param toastLayout 用于toast的布局,里面要包含一个id为R.id.slib_toast_text_1的TextView
     */
    public static void setToastLayout(int toastLayout, int gravity) {
        mToastLayoutId = toastLayout;
        TOAST_GRAVITY = gravity;
    }

    public static String getString(int id) {
        return context.getString(id);
    }

    public static int getVersion() {
        try {
            return getPackageInfo().versionCode;
        } catch (NullPointerException e) {
            return 0;
        }

    }


    public static PackageInfo getPackageInfo() {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 弹出一个toast,可以在任一线程使用
     *
     * @param o toast的内容
     */
    public static void toast(Object o) {
        if (null == o)
            return;
        //要toast的信息

        final String msg = String.valueOf(o);
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                createToast(msg);
            }
        });
    }

    private static void createToast(String msg) {
        if (mToast == null) {
            mToast = new Toast(context);
            if (mToastLayoutId == 0) {
                mToastLayoutId = R.layout.toast_layout;
            }
            View v = View.inflate(context, mToastLayoutId, null);
            mToast.setView(v);
            if (TOAST_GRAVITY != Gravity.NO_GRAVITY) {
                mToast.setGravity(TOAST_GRAVITY, 0, 0);
            }

            mToast.setDuration(Toast.LENGTH_SHORT);
        }


        TextView tv = (TextView) mToast.getView().findViewById(R.id.slib_toast_text_1);
        if (tv != null) {
            tv.setText(msg);
        }
        mToast.show();
    }

    public static void toast(int resId) {

        try {
            toast(getString(resId));
        } catch (Exception e) {
            toast(Integer.valueOf(resId));
        }
    }

    @MainThread
    public static void toast_debug(Object o) {

        if (SApplication.DEBUG) {
            toast(o);
        }
    }

    public static Context getContext() {
        return context;
    }
    private static Handler mainHandler=new Handler(Looper.getMainLooper());

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
