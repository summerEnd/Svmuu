package com.slib.demo;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sp.lib.activity.SlibActivity;
import com.sp.lib.common.support.net.client.SHttpClient;
import com.sp.lib.common.support.net.client.SHttpProgressHandler;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.SLog;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class HttpTest extends SLIBTest {
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);

        tv = new TextView(this);
        tv.setText("http://developers.android.com/");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("start...");
                SRequest request = new SRequest("http://developers.android.com/");
                SHttpClient.getClient().get(v.getContext(),"http://developers.android.com/",request, new SHttpProgressHandler() {
                    @Override
                    public Dialog onCreateDialog() {
                        return null;
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        tv.setText("fail");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        tv.setText("fail");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        tv.setText("fail");
                    }

                    @Override
                    public void onCancel() {
                        super.onCancel();
                        tv.setText("onCancel");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        tv.setText("onFinish");
                    }
                });
            }
        });

        Button button = new Button(this);
        button.setText("cancel");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHttpClient.cancelAllRequests();
            }
        });

        layout.addView(tv);
        layout.addView(button);
        setContentView(layout);

    }
}
