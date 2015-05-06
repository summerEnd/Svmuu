package com.yjy998.ui.pop;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sp.lib.common.util.SLog;
import com.yjy998.http.Response;
import com.yjy998.http.YHttpClient;
import com.yjy998.http.YHttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class RSAUtil {


    public static void sign(final Context context, final String source, final Callback callback) {
        YHttpClient.getInstance().getRsa(new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                try {
                    JSONObject data = new JSONObject(response.data);
                    new RSAUtil(context, data.getString("exponent"), data.getString("modulus"), source, callback);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String exp;
    private String mod;
    private String psw;
    private Callback callback;

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    private RSAUtil(Context context, String exp, String mod, String psw, Callback callback) {
        this.exp = exp;
        this.mod = mod;
        this.psw = psw;
        this.callback = callback;
        WebView webView = new WebView(context);
        webView.loadUrl("http://www.yjy998.com/file/rsa.html");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new I(), "YJY");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    public interface Callback {
        public void onResult(String rsa);
    }

    private Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            callback.onResult((String) msg.obj);
            return false;
        }
    });

    private class I {
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
            Message message = handler.obtainMessage();
            message.obj = rsa;
            handler.sendMessage(message);
        }

        @JavascriptInterface
        public String getPsw() {
            return psw;
        }
    }
}
