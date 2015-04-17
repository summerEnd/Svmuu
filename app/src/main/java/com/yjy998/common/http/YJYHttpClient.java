package com.yjy998.common.http;

import com.sp.lib.common.support.net.client.SHttpClient;
import com.sp.lib.common.support.net.client.SRequest;

/**
 * 所有网络请求统一入口
 */
public final class YJYHttpClient {

    private static final String HOST = "https://yjy998.com/yjy/quote/stock/601899/trend_data";

    public static void post(SRequest request) {
        SHttpClient.post(request, new YJYHttpHandler());
    }

    public static void post(SRequest request, YJYHttpHandler handler) {
        SHttpClient.post(request, handler);
    }

    public static void get(SRequest request, YJYHttpHandler handler) {
        SHttpClient.get(request, handler);
    }

}
