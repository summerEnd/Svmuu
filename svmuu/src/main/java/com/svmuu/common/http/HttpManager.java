package com.svmuu.common.http;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.SLog;
import com.sp.lib.widget.slide.menu.MenuDrawer;
import com.svmuu.AppDelegate;

import org.apache.http.client.ResponseHandler;

/**
 * 应用的所有Http请求原则上应该都经过这个类发起
 */
public class HttpManager {
    private static HttpManager instance;
    private AsyncHttpClient client = new AsyncHttpClient();


    private static SparseArray<String> HOSTS = new SparseArray<>();


    static {
        HOSTS.put(0, "http://dev.svmuu.com");//dev
        HOSTS.put(1, "http://m-dev.svmuu.com");//h5移动版
        HOSTS.put(2, "http://dev-test.svmuu.com");//test
        HOSTS.put(3, "http://mtest.svmuu.com");//h5移动版
        HOSTS.put(4, "http://www.svmuu.com");//正式环境
    }

    /**
     * 切换host
     */
    public static void setType(int type) {
        HttpManager.type = type;
    }

    public static int getType() {
        return type;
    }

    /**
     * todo host类型
     * 测试：2
     * 开发：0
     * 正式：4
     */
    private static int type = 0;

    public String getHost() {
        return HOSTS.get(type);
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
     * @param requestHandle 要处理的请求
     * @return true 已经取消
     */
    public boolean cancel(RequestHandle requestHandle) {
        return !(requestHandle != null && !requestHandle.isCancelled() && !requestHandle.isFinished()) || requestHandle.cancel(true);
    }

    /**
     * @param request request的url可以传完整的链接，也可以传除host以外的部分，程序会自动补全host
     */
    public RequestHandle postMobileApi(Context context, SRequest request, ResponseHandlerInterface handler) {
        fixUrl(request);

        if (context == null) {
            context = AppDelegate.getInstance();
        }
        String url = request.getUrl();
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        return client.post(context, url, request, handler);
    }


    /**
     * @param request request的url可以传完整的链接，也可以传除host以外的部分，程序会自动补全host
     */
    public void getMobileApi(@Nullable Context context, SRequest request, ResponseHandlerInterface handlerInterface) {
        fixUrl(request);
        String url = request.getUrl();
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (context == null) {
            context = AppDelegate.getInstance();
        }
        client.get(context, url, request, handlerInterface);
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
