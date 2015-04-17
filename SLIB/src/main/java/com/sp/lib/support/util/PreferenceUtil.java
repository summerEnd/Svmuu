package com.sp.lib.support.util;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class PreferenceUtil {
    public static SharedPreferences getPreference(Class cls) {
        SharedPreferences sp = ContextUtil.getContext().getSharedPreferences(cls.getSimpleName(), Context.MODE_PRIVATE);
        return sp;
    }
}
