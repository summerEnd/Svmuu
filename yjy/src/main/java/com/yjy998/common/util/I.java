package com.yjy998.common.util;

import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;


public class I {
    private String exp;
    private String mod;
    private String psw;
    private Handler mHandler;

    public I(Handler handler, String exp, String mod, String psw) {
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
