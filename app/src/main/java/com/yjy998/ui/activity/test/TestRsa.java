package com.yjy998.ui.activity.test;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sp.lib.common.admin.RSA;
import com.yjy998.ui.activity.YJYActivity;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;


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

        layout.addView(getTextView("modulus:" + modulus));
        layout.addView(getTextView("result:" + result));
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
                try {
                    byte[] bytes = RSA.encryptData("ldp8718".getBytes(), RSA.getPrivateKey(mInt, eInt));
                    resultText.setText("testResult:" + bcd2Str(bytes));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }
            }
        });
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
                try {
                    byte[] bytes = RSA.encryptData(password.getBytes(), RSA.getPrivateKey(mInt, eInt));
//                    resultText.setText("testResult:" + bcd2Str(bytes));
//                    RSAPublicKey publicKey = RSA.getPublicKey(mInt, eInt);
//                    String privateKey = publicKey.toString();
//

                    resultText.setText("testResult:" +bcd2Str(bytes) );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * BCD转字符串
     */
    public static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    private static final String ALGORITHM = "RSA";

    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private static final String DEFAULT_CHARSET = "UTF-8";

    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] sign(String content, RSAPrivateKey key) {
        java.security.Signature signature = null;
        try {
            signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);
            signature.initSign(key);
            signature.update(content.getBytes(DEFAULT_CHARSET));

            byte[] signed = signature.sign();

            return signed;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;

    }


}
