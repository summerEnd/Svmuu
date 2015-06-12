package com.yjy998.ui.activity.admin;

import android.app.Dialog;
import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ProtocolDialog extends Dialog {
    public ProtocolDialog(Context context) {
        super(context);
        WebView webView = new WebView(context);
        setContentView(webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl("http://m.yjy998.com/treaty.html");
    }
}
