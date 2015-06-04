package com.yjy998.common.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.SLog;
import com.yjy998.AppDelegate;

/**
 * 所有网络请求统一入口
 */
public final class YHttpClient {

    private static YHttpClient instance;
    private AsyncHttpClient client = new AsyncHttpClient();

    private final String HOST = "http://mobile.yjy998.com";

    public static YHttpClient getInstance() {
        if (instance == null) {
            //做一些初始化操作
            instance = new YHttpClient();
            AsyncHttpClient client = instance.getClient();
            client.setTimeout(30000);
            //client.setCookieStore(new PersistentCookieStore(AppDelegate.getInstance()));
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
        SLog.debug(request.toLogString());
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

    public final void cancel(Context context) {
        client.cancelRequests(context, true);
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
    public final void getByMethod(String method, SRequest params, YHttpHandler httpHandler) {
        params.setUrl(HOST + method);
        get(params, httpHandler);
    }

    public final void getByMethod(Context context, String method, SRequest params, YHttpHandler httpHandler) {
        params.setUrl(HOST + method);
        get(context, params, httpHandler);
    }

    /**
     * 获取股票价格
     */
    public void getStockPrice(String code, YHttpHandler handler) {
        SRequest request = new SRequest();
        request.setUrl("http://www.yjy998.com/stock/getstockprice");
        request.put("code", code);
        get(request, handler);
    }

    /**
     * 加密密码
     */
    public void getRsa(YHttpHandler handler) {
        SRequest request = new SRequest();
        request.setUrl("http://interface.yjy998.com/yjy/sec/rsa?_=" + Math.random());
        get(request, handler);
    }

    /**
     * 接口参数：
     * login_name=15951996518
     * login_passwd=123456
     * login_rsapwd=4167dfca...
     */
    public void login(Context context, SRequest request, YHttpHandler handler) {
        request.setUrl("http://www.yjy998.com/account/login");
        post(context, request, handler);
    }

    /**
     * 获取验证码
     */
    public void getCode(Context context, String phone, YHttpHandler handler) {
        SRequest request = new SRequest("http://www.yjy998.com/account/getloginpwdcode");
        request.put("phone", phone);
        request.put("code", 6);
        post(context, request, handler);
    }

    public void logout(YHttpHandler handle) {
        SRequest request = new SRequest("http://www.yjy998.com/account/logout?exit=1");
        post(request, handle);
    }

    public void getContractInfo(Context context, String contract_id, YHttpHandler handler) {
        SRequest request = new SRequest();
        request.put("contract_no", contract_id);
        //http://mobile.yjy998.com/h5/account/contractinfo
        YHttpClient.getInstance().getByMethod(context, "/h5/account/contractinfo", request, handler);
    }
}
