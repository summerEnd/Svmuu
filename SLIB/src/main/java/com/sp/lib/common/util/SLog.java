package com.sp.lib.common.util;

import android.util.Log;

import com.sp.lib.SApplication;

public class SLog {
    private static String TAG = "SLOG";

    public static final void debug_format(String pattern, Object... value) {
        debug(String.format(pattern, value));
    }

    public static final void debug(Object value) {
        log(TAG, String.valueOf(value));
    }

    public static void log(String tag, String value) {
        if (SApplication.DEBUG) {
            Log.d(tag, String.valueOf(value));
        }
    }
}
