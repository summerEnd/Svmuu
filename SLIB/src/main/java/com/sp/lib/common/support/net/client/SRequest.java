package com.sp.lib.common.support.net.client;

import android.util.Log;

import com.loopj.android.http.RequestParams;

import org.apache.http.message.BasicNameValuePair;

import java.util.List;

/**
 * {@link SHttpClient} request。
 */
public class SRequest extends RequestParams {
    String url;

    public SRequest(String url){
        setUrl(url);
    }

    public SRequest() {
    }

    public String getUrl() {
        return url;
    }
    public String getUrlWithParams(){
        List<BasicNameValuePair> paramsList = getParamsList();
        StringBuilder sb=new StringBuilder(url);
        sb.append("?");
        for (BasicNameValuePair pair:paramsList){
            sb.append(pair.getName());
            sb.append("=");
            sb.append(pair.getValue());
            sb.append("&");
        }
        return sb.toString();
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
        return builder.toString();
    }
}
