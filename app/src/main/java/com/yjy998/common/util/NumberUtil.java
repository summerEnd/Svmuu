package com.yjy998.common.util;

import java.text.DecimalFormat;

public class NumberUtil {
    public static String format(float number) {
        DecimalFormat format = new DecimalFormat();
        format.setGroupingSize(3);
        return format.format(number);
    }

    /**
     * 尝试将数字字符串转化为如下格式：12,345.45
     */
    public static String formatStr(String number) {
        String money;
        try {
            money = format(Float.parseFloat(number));
        } catch (NumberFormatException e) {
            money = number;
        }
        return money;
    }
    public static float getFloat(String number){
        try {
            return Float.parseFloat(number);
        } catch (NumberFormatException e) {
            return 0f;
        }
    }
}
