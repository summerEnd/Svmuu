package com.sp.lib.common.support.net.client;

import android.util.Log;

import com.loopj.android.http.RequestParams;

/**
 * {@link SHttpClient} request。
 */
public class SRequest extends RequestParams {
    String url;

    public SRequest() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * change the string to a readable string
     */
    public String toLogString() {
        //将RequestParams转化为字符串
        String[] strings = toString().split("&");
        StringBuilder builder = new StringBuilder();
        builder.append(url + "{\n");
        for (int i = 0; i < strings.length; i++) {
            builder.append(strings[i] + "\n");
        }
        builder.append("}");
        Log.i("SHTTP", builder.toString());
        return builder.toString();
    }
}
