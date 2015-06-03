package com.yjy998.common.test;

import android.test.AndroidTestCase;
import android.text.TextUtils;
import android.util.Log;

import com.sp.lib.common.admin.RSA;
import com.sp.lib.common.util.SLog;
import com.yjy998.common.util.NumberUtil;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Test extends AndroidTestCase {
    private static final String TAG = "test->";

    public void testRsa() {
        try {
            String modulus = "0084c8f5f42df0a41c2df15ea84c658e62b34ef07cacbb499f534b3391dd7590caf078949d249a4078267660964f703efcd1e045848156bb2282e26df15f21933c7a29e883565371c377111c94bd6f2b9439e48dd9b5ea88e7550045c9f8941239ae84963e27a9b4bd4b94c7f5bb2fdb203e8be88ab643d19e43e36e8ff77dbaa1";
            String exponent = "010001";
            String password = "ldp8718";
            String encrypt = RSA.encrypt(password, modulus, exponent);
            System.out.println(TAG + encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testStringCompare() {
        String str1 = "1.0.0";
        String str2 = "1.0.1";
        String str3 = "1.2.0";
        String str4 = "1.2";
        String str5 = "2.0";
        String str6 = "1.0.0.1";
        String str7 = "1.0";
        print(str1, str2);
        print(str1, str3);
        print(str1, str4);
        print(str1, str5);
        print(str1, str6);
        print(str1, str7);
    }

    public void testFormat() {
        //测试用例
        printFormat("null");
        printFormat("");
        printFormat("33");
        printFormat("33.0");
        printFormat("33.00");
        printFormat("33.44");
        printFormat("33.412314");
        printFormat("33.419314");
    }

    //测试方法
    void printFormat(String profit) {
        int floatPart;//小数部分
        int intPart;//整数部分
        try {
            BigDecimal decimal = new BigDecimal(profit);
            intPart = decimal.intValue();
            floatPart = (int) ((decimal.floatValue() - intPart) * 100);
        } catch (NumberFormatException e) {
            Log.e("FORMAT_", "Exception:" + profit);
            intPart = 0;
            floatPart = 0;
        }
        String floatStr = floatPart + "";

        SLog.log("FORMAT_", String.format("before:%s after:%d.%02d", profit, intPart, floatPart));
    }

    void print(String str1, String str2) {
        System.out.println(String.format(TAG + "%s compare %s=%d", str1, str2, str1.compareTo(str2)));
    }

    public void testFormatStr() {
        print("15654654654.11");
        print("156546546.11");
        print("46.11");
        print("0.131");
    }

    void print(String src) {
        log(NumberUtil.formatStr(src));
    }

    public void testDate() {
        log(getDate("095560"));
        log(getDate("95544"));
        log(getDate("94029"));
        log(getDate("142644"));
        log(getDate("150936"));

    }

    String getDate(String src) {
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

    void log(String msg) {
        System.out.println(TAG + msg);
    }
}
