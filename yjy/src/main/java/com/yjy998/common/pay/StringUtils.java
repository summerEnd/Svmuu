package com.yjy998.common.pay;

import android.text.TextUtils;

/**
 * Created by Lincoln on 15/6/17.
 */
public class StringUtils {
    public static String trimToEmpty(String sign) {
        return TextUtils.isEmpty(sign)?"":sign.trim();
    }

    public static boolean equals(String key, String sign) {
        if (key==null){
           return sign==null;
        }
        return key.equals(sign);
    }

    public static boolean isNotEmpty(String hmacKey) {
        return !TextUtils.isEmpty(hmacKey);
    }
}
