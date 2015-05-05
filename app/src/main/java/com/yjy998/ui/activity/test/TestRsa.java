package com.yjy998.ui.activity.test;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yjy998.ui.activity.YJYActivity;

import java.math.BigInteger;


public class TestRsa extends YJYActivity {
    String result = "73c3deb2f89d08647d6311ac513a2538829ed329c1ac8413e0074ea735d0eeabffd17f08acd8bce9431802bde2e2614a0073d95fc1d80bdceb5ca9c053ccf472846acea88dab76f5c529b1efd4b4fa5e33a88e2c52c6a1cf4acf2dd1908f2850f0db502a7fe9082a60c80ee0c314a04864db55cce11900976ff293965ea7af13";
    String modulus = "84c8f5f42df0a41c2df15ea84c658e62b34ef07cacbb499f534b3391dd7590caf078949d249a4078267660964f703efcd1e045848156bb2282e26df15f21933c7a29e883565371c377111c94bd6f2b9439e48dd9b5ea88e7550045c9f8941239ae84963e27a9b4bd4b94c7f5bb2fdb203e8be88ab643d19e43e36e8ff77dbaa1";
    String exponent = "10001";
    TextView resultText;
    String password = "ldp8718";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        scrollView.addView(layout);
        setContentView(scrollView);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(getTextView("modulus:\n" + modulus));
        layout.addView(getTextView("result:\n" + result));
        resultText = getTextView("testResult:");

        layout.addView(resultText);
        add(layout, 2);
        add(layout, 8);
        add(layout, 16);
        add(layout, 10);

        Button button = new Button(this);
        button.setText("radix:no");
        layout.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BigInteger mInt = new BigInteger(modulus, 16);
                BigInteger eInt = new BigInteger(exponent);
                start(mInt, eInt);
            }
        });
    }

    void start(BigInteger mInt, BigInteger eInt) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 拆分字符串
     */
    public static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i = 0; i < x + z; i++) {
            if (i == x + z - 1 && y != 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

    TextView getTextView(String text) {
        TextView textView = new TextView(this);
        textView.setPadding(10, 10, 10, 10);
        textView.setTextSize(12);
        textView.setText(text);
        return textView;
    }

    void add(ViewGroup layout, final int radix) {
        Button button = new Button(this);
        button.setText("radix:" + radix);
        layout.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BigInteger mInt = new BigInteger(modulus, 16);
                BigInteger eInt = new BigInteger(exponent, radix);
                start(mInt, eInt);
            }
        });
    }




}
