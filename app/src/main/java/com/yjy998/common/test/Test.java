package com.yjy998.common.test;

import android.test.AndroidTestCase;

import com.sp.lib.common.admin.RSA;

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

    void print(String str1, String str2) {
        System.out.println(String.format(TAG + "%s compare %s=%d", str1, str2, str1.compareTo(str2)));

    }
}
