package com.svmuu;

import android.app.Dialog;
import android.test.AndroidTestCase;

import com.sp.lib.common.support.net.client.SHttpClient;
import com.sp.lib.common.support.net.client.SHttpProgressHandler;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.SLog;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by Lincoln on 15/6/17.
 */
public class MyTest extends AndroidTestCase {
    public void testPay() {
        final SRequest request = new SRequest("http://mobiletest.yeepay.com/paymobile/api/pay/request");
        SHttpClient.post(request, new SHttpProgressHandler() {
            @Override
            public Dialog onCreateDialog() {
                return null;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                SLog.debug(response);

            }

            @Override
            public void onFinish() {
                super.onFinish();
                SLog.debug("finish");
            }
        });
    }

    public void testA() {
        int[] a = new int[]{1, 3, 5, 7, 9, 11, 13, 15};
        int count=a.length;
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                for (int k = 0; k < count; k++) {
                    if (a[i]+a[j]+a[k]==30){
                        System.out.println(a[i]+" "+a[j]+" "+a[k]);
                    }
                }
            }
        }
    }
}
