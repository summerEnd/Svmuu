package com.yjy998.common.preference;

import android.content.Context;

import com.yjy998.AppDelegate;

public class CookiePreference extends YPreference {
    public static final String COOKIE="Set-Cookie";
    public static final CookiePreference get() {
        return new CookiePreference(AppDelegate.getInstance());
    }

    public CookiePreference(Context context) {
        super(context);
    }


    public String getCookie() {
        return getPreference().getString("cookie", null);
    }

    public void setCookie(String cookie) {
        getEditor().putString("cookie", cookie).commit();
    }
}
