package com.sp.lib.activity;

import com.sp.lib.support.net.SHttpClient;

import org.json.JSONObject;

/**
 * 1.加载网络数据
 * 2.加载失败页面
 * 3.加载进度条
 */
public abstract class NetWorkActivity extends BaseActivity {



    /**
     * 开始网络任务
     */
    protected void start(NetworkInfo info) {
        SHttpClient.post(info.url, info.params);
    }

    protected abstract void onSuccess(JSONObject object);

    protected void onFail(String msg) {

    }
}
