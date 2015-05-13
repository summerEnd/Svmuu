package com.yjy998.ui.activity.main.more;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sp.lib.common.util.SLog;
import com.yjy998.R;
import com.yjy998.ui.activity.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class MoreFragment extends BaseFragment {

    View layout;
    WebView webView;
    private boolean initialLoad = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_more, container, false);
        webView = (WebView) layout.findViewById(R.id.webView);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView.loadUrl("http://m.yjy998.com/faq.html");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                initialLoad = newProgress < 100;
            }

        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                SLog.debug(url);
                if (!initialLoad) {
                    startActivity(new Intent(getActivity(), WebViewActivity.class)
                            .putExtra(WebViewActivity.EXTRA_URL, url));
                }
                return true;
            }


        });

    }
}
