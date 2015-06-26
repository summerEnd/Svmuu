package com.svmuu.common.config;


import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

    String PREF_NAME="pref_name";

    public static SharedPreferences get(Context context,Class<USER> clazz){
        return context.getSharedPreferences(clazz.getSimpleName(),Context.MODE_PRIVATE);
    }

    public interface USER{
        String USER_PASSWORD = "psw";
        String USER_NAME = "name";
        String IS_SAVE_PASSWORD = "sv_psw";
    }

}
