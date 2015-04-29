package com.sp.lib.common.support.net.client;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.sp.lib.R;
import com.sp.lib.common.util.ContextUtil;

public class SHttpClient {

    private static final String TAG = "SHttp";
    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        client.setTimeout(10000);
    }

    public static AsyncHttpClient getClient() {
        return client;
    }

    /**
     * post http request
     */
    public static void post(SRequest params, AsyncHttpResponseHandler handler) {
        if (params == null) {
            return;
        }

        if (!isConnect(ContextUtil.getContext())) {
            ContextUtil.toast(R.string.network_is_not_open);
            return;
        }
        client.post(params.getUrl(), params, handler);
    }

    public static void get(SRequest params, AsyncHttpResponseHandler handler) {
        client.get(params.getUrl(), params, handler);
    }

    public static void cancelAllRequests() {
        client.cancelAllRequests(true);

    }

    /**
     * indicates whether network connectivity exists and it is possible to establish
     * connections and pass data. Always call this before attempting to perform data transactions.
     *
     * @return {@code true} if network connectivity exists, {@code false} otherwise.
     */
    public static boolean isConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                // 判断当前网络是否已经连接
                if (info != null && info.isConnected()) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.w(TAG, e.toString());
        }
        return false;
    }
}
