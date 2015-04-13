package com.sp.lib.support.net;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SHttpClient {

    private static final String TAG = "SLOG";
    private static Callback mCallback;
    private static Dialog   mDialog;
    private static AsyncHttpClient client = new AsyncHttpClient();


    static {
        client.setTimeout(10000);
    }

    public static void setHandler(Callback callback) {
        SHttpClient.mCallback = callback;
    }

    /**
     * @param url
     * @param params
     * @see android.graphics.BitmapFactory
     */
    public static void post(String url, RequestParams params) {

        if (mCallback != null && mCallback.showLogs()) {
            //打印参数
            if (params == null) {
                Log.i("SHTTP", "null");
            } else {
                //将RequestParams转化为字符串
                String[] strings = params.toString().split("&");
                StringBuilder builder = new StringBuilder();
                builder.append(url + "{\n");
                for (int i = 0; i < strings.length; i++) {
                    builder.append(strings[i] + "\n");
                }
                builder.append("}");
                Log.i("SHTTP", builder.toString());
            }
        }
        showProgress();
        client.post(url, params, new Utf8JsonHandler());
    }

    /**
     * 用dialog creator创建一个dialog
     */
    private static void showProgress() {
        if (mCallback == null) {
            return;
        }
        mDialog = mCallback.onCreateProgressDialog();
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public static boolean isConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            Log.v("error", e.toString());
        }
        return false;
    }


    public static abstract class Callback {
        /**
         * 创建一个Dialog，当网络请求发起时弹出，请求结束时关闭
         *
         * @return null不弹出dialog
         */
        public abstract Dialog onCreateProgressDialog();

        /**
         * @return true 在控制台打印请求信息，false 反之
         */
        public abstract boolean showLogs();

        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            log(responseString);
        }

        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            log(errorResponse);
        }

        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            log(errorResponse);
        }

        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            log(response);
        }

        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            log(response);
        }

        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            log(responseString);
        }

        final void log(Object o) {
            if (mCallback != null && mCallback.showLogs()) {
                String log;
                if (o == null) {
                    log = "null";
                } else if (o instanceof JSONObject) {
                    try {
                        log = ((JSONObject) o).toString(4);
                    } catch (JSONException e) {
                        log = String.valueOf(o);
                    }
                } else {
                    log = String.valueOf(o);
                }

                Log.i("SHttp", log);
            }
        }
    }

    private static class Utf8JsonHandler extends JsonHttpResponseHandler {

        private Utf8JsonHandler() {
            super("UTF-8");
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            if (mCallback != null) {
                mCallback.onFailure(statusCode, headers, responseString, throwable);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            if (mCallback != null) {
                mCallback.onFailure(statusCode, headers, throwable, errorResponse);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            if (mCallback != null) {
                mCallback.onFailure(statusCode, headers, throwable, errorResponse);
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            if (mCallback != null) {
                mCallback.onSuccess(statusCode, headers, response);
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            if (mCallback != null) {
                mCallback.onSuccess(statusCode, headers, response);
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            if (mCallback != null) {
                mCallback.onSuccess(statusCode, headers, responseString);
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            if (mDialog != null) {
                mDialog.dismiss();
            }
        }
    }

}
