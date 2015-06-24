package com.svmuu.common.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;
import com.sp.lib.common.support.net.client.SRequest;

import org.apache.http.client.ResponseHandler;

/**
 * Created by Lincoln on 15/6/24.
 */
public class HttpManager {
    private static HttpManager instance;
    private AsyncHttpClient client = new AsyncHttpClient();

    private final String HOST = "http://mobile.yjy998.com";

    public static HttpManager getInstance() {
        if (instance == null) {
            //做一些初始化操作
            instance = new HttpManager();
            AsyncHttpClient client = instance.getClient();
            client.setTimeout(30000);
            //client.setCookieStore(new PersistentCookieStore(AppDelegate.getInstance()));
        }
        return instance;
    }

    public AsyncHttpClient getClient() {
        return client;
    }

    public void post(SRequest request, ResponseHandlerInterface handler) {
        client.post(request.getUrl(), request, handler);
    }
}
