package com.sp.lib.common.support.update;

/**
 * update the app
 */
public class UpdateManager {

    public static void start(Callback callback, UpdateHandler handler) {
        handler.setUrl(callback.getDownloadUrl());
        if (!callback.isNewestVersion()) {
            handler.noticeNewest();
            return;
        }


        if (callback.forceUpdate()) {
            handler.noticeForceUpdate();
        } else {
            handler.noticeUpdate();
        }
    }

    public interface Callback {
        /**
         * 是否为最新版本
         */
        public boolean isNewestVersion();

        /**
         * @return true 强制升级
         */
        public boolean forceUpdate();

        /**
         * 获取下载链接
         */
        public String getDownloadUrl();
    }
}
