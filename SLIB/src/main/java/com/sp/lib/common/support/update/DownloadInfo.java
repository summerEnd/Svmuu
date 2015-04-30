package com.sp.lib.common.support.update;

public class DownloadInfo {
    public long downloadId;
    public long download_so_far;
    public long total;
    public int state;

    /**
     * 获取下载进度，取值范围0-1
     */
    public float getProgress() {
        return download_so_far / (float) total;
    }
}
