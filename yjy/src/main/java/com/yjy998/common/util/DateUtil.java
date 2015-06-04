package com.yjy998.common.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class DateUtil {
    /**
     * 主要针对本项目中后台返回的日期格式进行解析和格式化。
     * 注：后台返回的日期一般为5或者6位数字
     */
    public static String formatDate(String src){
        if (TextUtils.isEmpty(src)) {
            return "";
        }
        SimpleDateFormat sdf;
        if (src.length() == 5) {
            sdf = new SimpleDateFormat("Hmmss", Locale.getDefault());
        } else {
            sdf = new SimpleDateFormat("HHmmss", Locale.getDefault());
        }
        try {
            return new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(sdf.parse(src));
        } catch (ParseException e) {

        }
        return "";
    }
}
