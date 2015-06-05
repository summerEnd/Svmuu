package com.yjy998.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberUtil {
    public static String format(double number) {
        DecimalFormat format = (DecimalFormat) NumberFormat.getInstance();
        format.setGroupingSize(3);
        return format.format(number);
    }

    /**
     * 尝试将数字字符串转化为如下格式：12,345.45
     */
    public static String formatStr(String number) {
        String money;
        try {
            BigDecimal decimal=new BigDecimal(number);
            money = format(decimal.doubleValue());
        } catch (NumberFormatException e) {
            money = number;
        }
        return money;
    }

    /**
     * 从字符串中解析数字，如果解析失败返回0f
     */
    public static double getDouble(String number) {
        try {
            return new BigDecimal(number).doubleValue();
        } catch (NumberFormatException e) {
            return 0f;
        }
    }
}
