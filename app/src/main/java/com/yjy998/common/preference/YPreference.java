package com.yjy998.common.preference;

import android.content.Context;
import android.content.SharedPreferences;


public class YPreference {
    private SharedPreferences sp;

    public YPreference(Context context) {
        sp = context.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE);
    }

    public SharedPreferences.Editor getEditor() {
        return sp.edit();
    }

    public SharedPreferences getPreference(){
        return sp;
    }

}
