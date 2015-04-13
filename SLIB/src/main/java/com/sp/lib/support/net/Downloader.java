package com.sp.lib.support.net;


import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED;

/**
 * 基于{@link android.app.DownloadManager}，可查看相关技术文档
 */
public class Downloader {


    public static long start(Context context, String url, File saveFile) {
        return start(context, url, saveFile,null,null);
    }

    public static long start(Context context, String url, File saveFile, CharSequence title, CharSequence description) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(saveFile))
                .allowScanningByMediaScanner();
        if (TextUtils.isEmpty(title)) {
            request.setTitle(title);
        }
        if (TextUtils.isEmpty(description)) {
            request.setDescription(description);
        }
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        return manager.enqueue(request);
    }



}
