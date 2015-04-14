package com.yjy998.client;

import android.app.Dialog;

import com.sp.lib.support.net.client.SHttpClient;
import com.sp.lib.support.net.client.SHttpProgressHandler;
import com.sp.lib.support.net.client.SRequest;

/**
 * 所有网络请求统一入口
 */
public final class YJYHttpClient {

    public static void post(SRequest request) {
        SHttpClient.post(request, new YJYHttpHandler());
    }

    public static void post(SRequest request, YJYHttpHandler handler) {
        SHttpClient.post(request, handler);
    }

}
