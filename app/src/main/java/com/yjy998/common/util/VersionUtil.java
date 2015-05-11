package com.yjy998.common.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.support.update.DownloadInfo;
import com.sp.lib.common.support.update.Downloader;
import com.sp.lib.common.support.update.UpdateManager;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.JsonUtil;
import com.yjy998.R;
import com.yjy998.common.entity.UpdateInfo;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.pop.YAlertDialog;
import com.yjy998.ui.pop.YAlertDialogTwoButton;

import java.util.ArrayList;

public class VersionUtil {

    public static void start(final Context context) {

        YHttpClient.getInstance().get(context, new SRequest("http://mobile.yjy998.com/h5/version/check"), new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                UpdateInfo info = JsonUtil.get(response.data, UpdateInfo.class);
                if (info != null) {
                    UpdateManager.start(new VersionCallback(context, info));
                }
            }
        });

    }

    private static class VersionCallback implements UpdateManager.Callback {
        Context context;
        private ProgressDialog dialog;
        private Downloader downloader;
        private UpdateInfo info;

        public VersionCallback(Context context, UpdateInfo info) {
            this.info = info;
            this.context = context;
            dialog = new ProgressDialog(context);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(100);
            downloader = new Downloader();
        }

        @Override
        public boolean isNewestVersion() {
            return info.version.compareTo(ContextUtil.getPackageInfo().versionName) <=0;
        }

        @Override
        public boolean forceUpdate() {
            return info.force;
        }

        @Override
        public String getDownloadUrl() {
            return info.download;
        }

        @Override
        public void noticeNewest() {
            YAlertDialog yAlertDialog = new YAlertDialog(context);
            yAlertDialog.setMessage(context.getString(com.sp.lib.R.string.alread_newest));

            yAlertDialog.show();
        }

        @Override
        public void noticeUpdate() {
            YAlertDialogTwoButton yAlertDialog = new YAlertDialogTwoButton(context);
            yAlertDialog.setMessage(context.getString(R.string.find_new_version));
            yAlertDialog.setCancelable(false);
            yAlertDialog.setButton1(com.sp.lib.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doUpdate();
                    dialog.dismiss();
                }
            });
            yAlertDialog.setButton2(com.sp.lib.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            yAlertDialog.show();
        }

        @Override
        public void noticeForceUpdate() {
            YAlertDialogTwoButton yAlertDialog = new YAlertDialogTwoButton(context);
            yAlertDialog.setMessage(context.getString(R.string.find_new_version));
            yAlertDialog.setCancelable(false);
            yAlertDialog.setButton1(com.sp.lib.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doUpdate();
                    dialog.dismiss();
                }
            });

            yAlertDialog.show();
        }

        protected void doUpdate() {
            try {
                long id = downloader.downloadWithNotification(getDownloadUrl(), null, null, "hello", "hello");
                context.getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, new MyObserver(new Handler(), id));
            } catch (Exception e) {
                ContextUtil.toast_debug(context.getString(R.string.download_failed));
            }

        }

        protected void publishProgress(DownloadInfo info) {
            dialog.setProgress((int) (info.getProgress() * 100));
        }

        private class MyObserver extends ContentObserver {

            long id;

            public MyObserver(Handler handler, long id) {
                super(handler);
                this.id = id;
            }

            @Override
            public void onChange(boolean selfChange) {
                ArrayList<DownloadInfo> downloadInfos = downloader.getDownloadInfos(context, id);
                publishProgress(downloadInfos.get(0));
            }
        }
    }
}
