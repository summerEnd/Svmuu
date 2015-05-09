package com.yjy998.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class RSAUtil {

    private static RSAUtil rsaUtil;

    public static void sign(final Context context, final String source, final Callback callback) {
        if (rsaUtil == null) {
            rsaUtil = new RSAUtil();
        }

        //调用接口获取exponent和modulus
        YHttpClient.getInstance().getRsa(new YHttpHandler(false) {
            @Override
            protected void onStatusCorrect(Response response) {
                try {
                    JSONObject data = new JSONObject(response.data);
                    rsaUtil.sign(context, data.getString("exponent"), data.getString("modulus"), source, callback);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onStatusFailed(Response response) {
                callback.onRSAEncodeFailed();
            }
        });
    }

    private Callback callback;

    public RSAUtil() {
    }

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    private void sign(Context context, String exp, String mod, String psw, Callback callback) {

        this.callback = callback;
        WebView webView = new WebView(context);
        webView.loadUrl("http://www.yjy998.com/file/rsa.html");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new I(handler, exp, mod, psw), "YJY");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    /**
     * 加密回调
     */
    public interface Callback {
        /**
         * 加密成功
         *
         * @param rsa rsa加密后的字符串
         */
        public void onRSAEncodeSuccess(String rsa);

        /**
         * 加密失败
         */
        public void onRSAEncodeFailed();
    }

    private Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            callback.onRSAEncodeSuccess((String) msg.obj);
            return false;
        }
    });

    /**
     * JS接口
     */
    private class I {

        private String exp;
        private String mod;
        private String psw;
        private Handler mHandler;

        private I(Handler handler, String exp, String mod, String psw) {
            this.exp = exp;
            this.mod = mod;
            this.psw = psw;
            this.mHandler = handler;
        }

        @JavascriptInterface
        public String getExp() {
            return exp;
        }

        @JavascriptInterface
        public String getMod() {
            return mod;
        }

        @JavascriptInterface
        public void setResult(String rsa) {
            Message message = mHandler.obtainMessage();
            message.obj = rsa;
            mHandler.sendMessage(message);
        }

        @JavascriptInterface
        public String getPsw() {
            return psw;
        }
    }
}
