package com.yjy998.common.util;

import java.math.BigDecimal;
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
            BigDecimal decimal=new BigDecimal(number);
            money = format(decimal.floatValue());
        } catch (NumberFormatException e) {
            money = number;
        }
        return money;
    }

    /**
     * 从字符串中解析数字，如果解析失败返回0f
     */
    public static float getFloat(String number) {
        try {
            return new BigDecimal(number).floatValue();
        } catch (NumberFormatException e) {
            return 0f;
        }
    }
}
