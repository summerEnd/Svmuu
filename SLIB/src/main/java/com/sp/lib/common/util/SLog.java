package com.sp.lib.common.util;

import android.util.Log;

import com.sp.lib.SApplication;

public class SLog {
    private static String TAG = "SLOG";

    public static final void debug_format(String pattern, Object... value) {
        debug(String.format(pattern, value));
    }

    public static final void debug(Object value) {
        if (SApplication.DEBUG) {
            Log.d(TAG, String.valueOf(value));
        }
    }
}
