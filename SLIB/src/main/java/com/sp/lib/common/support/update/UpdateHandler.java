package com.sp.lib.common.support.update;

import android.text.TextUtils;

import com.sp.lib.common.util.ContextUtil;

public abstract class UpdateHandler {

    private String url;

    public final void setUrl(String url) {
        this.url = url;
    }

    public final String getUrl() {
        return url;
    }

    /**
     * 执行更新
     */
    public void doUpdate() {
        if (TextUtils.isEmpty(url)) {
            ContextUtil.toast_debug("invalid url:" + url);
            return;
        }
    }

    /**
     * 通知当前版本为最新版本
     */
    public abstract void noticeNewest();

    /**
     * 通知用户更新，并不执行更新
     */
    public abstract void noticeUpdate();

    public abstract void noticeForceUpdate();
}