package com.yjy998.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.SLog;
import com.yjy998.AppDelegate;

/**
 * 所有网络请求统一入口
 */
public final class YHttpClient {

    private static YHttpClient instance;
    private AsyncHttpClient client = new AsyncHttpClient();

    private final String HOST = "http://www.yjy998.com";

    public static YHttpClient getInstance() {
        if (instance == null) {
            //做一些初始化操作
            instance = new YHttpClient();
            instance.getClient().setTimeout(10000);
        }
        return instance;
    }

    public AsyncHttpClient getClient() {
        return client;
    }

    /**
     * 用post发起请求，使用application的Context
     */
    public void post(SRequest request, YHttpHandler handler) {
        post(AppDelegate.getInstance(), request, handler);
    }

    /**
     * 用post发起请求
     */
    public void post(Context context, SRequest request, YHttpHandler handler) {
        client.post(context, request.getUrl(), request, handler);
    }

    /**
     * 用get发起请求，使用application的Context
     */
    public void get(SRequest request, YHttpHandler handler) {
        get(AppDelegate.getInstance(), request, handler);
    }

    /**
     * 用get发起请求
     */
    public final void get(Context context, SRequest request, YHttpHandler httpHandler) {
        SLog.debug(request.toLogString());
        client.get(context, request.getUrl(), request, httpHandler);
    }

    /**
     * 取消所有网络请求
     */
    public final void cancelAll() {
        client.cancelAllRequests(true);
    }

    /**
     * post调用接口，不用传host，直接传方法
     */
    public final void postMethod(String method, SRequest params, YHttpHandler httpHandler) {
        params.setUrl(HOST + method);
        post(params, httpHandler);
    }

    /**
     * get调用接口，不用传host，直接传方法
     */
    public final void getMethod(String method, SRequest params, YHttpHandler httpHandler) {
        params.setUrl(HOST + method);
        get(params, httpHandler);
    }

    /**
     * 获取股票价格
     */
    public void getStockInfo(String code, YHttpHandler handler) {
        SRequest request = new SRequest();
        request.setUrl("http://www.yjy998.com/stock/getstockprice");
        request.put("code", code);
        get(request, handler);
    }
}
