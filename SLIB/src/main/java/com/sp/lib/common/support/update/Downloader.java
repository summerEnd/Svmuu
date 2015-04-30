package com.sp.lib.common.support.update;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.sp.lib.common.support.update.DownloadInfo;
import com.sp.lib.common.util.ContextUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Downloader {
    /**
     * 使用{@link android.app.DownloadManager DownloadManager}进行下载
     *
     * @param downloadUrl         下载链接
     * @param fileName            把下载文件名称改为fileName，如果不想改变就传null
     * @param saveDir             下载文件保存到的目录
     * @param notifyCationTitle   通知的标题
     * @param notifyCationMessage 通知的消息
     * @return
     */
    public long downloadWithNotification(String downloadUrl, File saveDir, String fileName, String notifyCationTitle, String notifyCationMessage) {
        Uri uri = Uri.parse(downloadUrl);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedOverRoaming(true);
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        request.setTitle(notifyCationTitle);
        request.setDescription(notifyCationMessage);

        if (saveDir == null) {
            saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }

        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        if (!TextUtils.isEmpty(fileName))
            request.setDestinationUri(Uri.fromFile(new File(saveDir, fileName)));
        DownloadManager manager = (DownloadManager) ContextUtil.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        return manager.enqueue(request);
    }

    /**
     * @param saveDir 保存目录
     * @param url     下载链接
     */
    public void download(File saveDir, String url) {
        download(saveDir, null, url);
    }

    /**
     * @param dir      保存目录
     * @param fileName 保存文件名
     * @param url      下载链接
     */
    public void download(File dir, String fileName, String url) {
        FileOutputStream fot = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setDoInput(true);
            InputStream in = conn.getInputStream();

            if (TextUtils.isEmpty(fileName)) {
                //根据conn获取文件名称
                fileName = getFileNameFromConn(conn);
            }

            if (TextUtils.isEmpty(fileName)) {
                //根据链接后缀取名
                fileName = url.substring(url.lastIndexOf("/") + 1);
            }

            File saveFile = new File(dir, fileName);
            fot = new FileOutputStream(saveFile);

            byte[] buffer = new byte[1024 * 10];
            int length;
            while ((length = in.read(buffer)) != -1) {
                fot.write(buffer, 0, length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fot != null) {
                try {
                    fot.flush();
                    fot.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param dir
     * @param fileName
     * @param url
     * @param breakPoint
     */
    public void download(File dir, String fileName, String url, boolean breakPoint) {
        File file = new File(dir, fileName);
        try {
            RandomAccessFile randomFile = new RandomAccessFile(file, "rw");
            URLConnection conn = new URL(url).openConnection();
            conn.setDoInput(true);

            //断点位置
            long offset;
            //如果不做断点下载就删除
            if (!breakPoint) {
                file.delete();
                offset = 0;
            } else if (!file.exists()) {
                offset = 0;
            } else {
                offset = file.length();
            }

            randomFile.seek(offset);
            conn.setRequestProperty("RANGE", "bytes=" + offset);

            InputStream in = conn.getInputStream();
            byte[] buffer = new byte[1024 * 10];
            int length;
            while ((length = in.read(buffer)) != -1) {
                randomFile.write(buffer, 0, length);
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取下载链接文件名称
     *
     * @param url
     * @return
     */
    public String getRemoteFileName(String url) {
        String fileName = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setDoInput(true);
            fileName = getFileNameFromConn(conn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(fileName)) {
            fileName = url.substring(url.lastIndexOf("/") + 1);
        }
        return fileName;
    }

    /**
     * 根据url获取文件名。耗时方法。
     *
     * @return 文件名
     */
    public String getFileNameFromConn(URLConnection conn) {
        String filename = null;
        // 从UrlConnection中获取文件名称

        Map<String, List<String>> headFields = conn.getHeaderFields();
        if (headFields == null) {
            return null;
        }
        Set<String> keySet = headFields.keySet();
        if (keySet == null) {
            return null;
        }
        //遍历headFields
        for (String key : keySet) {

            List<String> values = headFields.get(key);
            //遍历每一个参数列表
            for (String value : values) {
                value = value.trim();
                int location = value.indexOf("filename");
                if (location >= 0) {
                    //把filename之前包括filename舍掉，9是字符串"filename="的长度
                    filename = value.substring(location + 9).trim();
                }

                if (!TextUtils.isEmpty(filename)) {
                    return filename;
                }
            }
        }
        return filename;
    }

    public ArrayList<DownloadInfo> getDownloadInfos(Context context, long... ids) {
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Cursor c = manager.query(new DownloadManager.Query().setFilterById(ids));

        int _id = c.getColumnIndex(DownloadManager.COLUMN_ID);
        int _status = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int _down = c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
        int _total = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<DownloadInfo>();
        while (c.moveToNext()) {
            DownloadInfo p = new DownloadInfo();

            p.downloadId = c.getLong(_id);
            p.download_so_far = c.getLong(_down);
            p.total = c.getLong(_total);
            p.state= c.getInt(_status);
            downloadInfos.add(p);
        }
        c.close();
        return downloadInfos;
    }

}
