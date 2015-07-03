package com.svmuu.common.http;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.SLog;
import com.svmuu.AppDelegate;

/**
 * 应用的所有Http请求原则上应该都经过这个类发起
 */
public class HttpManager {
    private static HttpManager instance;
    private AsyncHttpClient client = new AsyncHttpClient();

    //todo 是否使用测试host
    private final  boolean  USE_TEST_HOST = true;

    public  String getHost() {
        if (USE_TEST_HOST) {
            return "http://dev-test.svmuu.com";
        } else {
            return "http://www.svmuu.com";
        }
    }

    public static HttpManager getInstance() {
        if (instance == null) {
            //做一些初始化操作
            instance = new HttpManager();
            AsyncHttpClient client = instance.getClient();
            client.setTimeout(30000);
        }
        return instance;
    }

    public AsyncHttpClient getClient() {
        return client;
    }


    /**
     * @param request request的url可以传完整的链接，也可以传除host以外的部分，程序会自动补全host
     */
    public void postMobileApi(Context context, SRequest request, ResponseHandlerInterface handlerInterface) {
        fixUrl(request);

        if (context == null) {
            context = AppDelegate.getInstance();
        }
        client.post(context, request.getUrl(), request, handlerInterface);
    }


    /**
     * @param request request的url可以传完整的链接，也可以传除host以外的部分，程序会自动补全host
     */
    public void getMobileApi(@Nullable Context context, SRequest request, ResponseHandlerInterface handlerInterface) {
        fixUrl(request);
        if (context == null) {
            context = AppDelegate.getInstance();
        }
        client.get(context, request.getUrl(), request,handlerInterface);
    }

    //修复url
    private void fixUrl(SRequest request) {

        String url = request.getUrl();

        if (TextUtils.isEmpty(url) || url.startsWith("http://")) {
            return;
        }

        request.setUrl(getHost() + "/moblieapi/" + url);
        //todo 发布
        SLog.debug(request.toLogString());

    }
}
