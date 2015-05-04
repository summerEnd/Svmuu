package com.sp.lib.common.preference;


import android.content.Context;
import android.content.SharedPreferences;

import com.sp.lib.common.util.ContextUtil;

public class PreferenceUtil {
    public static SharedPreferences getPreference(Class cls) {
        SharedPreferences sp = ContextUtil.getContext().getSharedPreferences(cls.getSimpleName(), Context.MODE_PRIVATE);

        return sp;
    }
}
