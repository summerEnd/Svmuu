package com.svmuu.common.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.SLog;

import org.apache.http.client.ResponseHandler;

/**
 * 应用的所有Http请求原则上应该都经过这个类发起
 */
public class HttpManager {
    private static HttpManager instance;
    private AsyncHttpClient client = new AsyncHttpClient();

    //是否使用测试host
    private static boolean USE_TEST_HOST = true;

    public static String getHost() {
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
    public void post(SRequest request, ResponseHandlerInterface handler) {
        String url = request.getUrl();
        if (!url.startsWith("http://")) {
            url = getHost() + url;
        }
        //todo 发布时最好注销
        SLog.debug(request.toLogString());
        client.post(url, request, handler);
    }
}
