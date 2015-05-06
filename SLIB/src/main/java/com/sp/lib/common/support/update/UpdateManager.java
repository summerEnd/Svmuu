package com.sp.lib.common.support.update;

/**
 * app检查更新
 */
public class UpdateManager {

    public static void start(Callback callback) {
        if (callback.isNewestVersion()) {
            callback.noticeNewest();
            return;
        }


        if (callback.forceUpdate()) {
            callback.noticeForceUpdate();
        } else {
            callback.noticeUpdate();
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

        /**
         * 通知当前版本为最新版本
         */
        public void noticeNewest();

        /**
         * 通知用户更新，并不执行更新
         */
        public void noticeUpdate();

        public void noticeForceUpdate();
    }
}
