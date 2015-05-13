package com.yjy998.common.util;

import java.text.DecimalFormat;

public class NumberUtil {
    public static String format(float number) {
        DecimalFormat format = new DecimalFormat();
        format.setGroupingSize(3);
        return format.format(number);
    }

    public static String formatStr(String number) {
        String money;
        try {
            money = format(Float.parseFloat(number));
        } catch (NumberFormatException e) {
            money = number;
        }
        return money;
    }
}
