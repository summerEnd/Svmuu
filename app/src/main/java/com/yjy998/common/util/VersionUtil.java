package com.yjy998.common.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;

import com.sp.lib.common.support.net.client.SRequest;
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
import com.yjy998.ui.pop.YDownloadDialog;

import java.io.File;

public class VersionUtil {

    public static void start(final Context context, final boolean noticeNewest) {

        YHttpClient.getInstance().get(context, new SRequest("http://mobile.yjy998.com/h5/version/check"), new YHttpHandler(false) {
            @Override
            protected void onStatusCorrect(Response response) {
                UpdateInfo info = JsonUtil.get(response.data, UpdateInfo.class);
                if (info != null) {
                    UpdateManager.start(new VersionCallback(context, info, noticeNewest));
                }
            }
        });

    }

    private static class VersionCallback implements UpdateManager.Callback {
        Context context;
        private UpdateInfo info;
        private boolean noticeNewest;

        public VersionCallback(Context context, UpdateInfo info, boolean noticeNewest) {

            this.noticeNewest = noticeNewest;
            this.info = info;
            this.context = context;

        }

        @Override
        public boolean isNewestVersion() {
            return info.version.compareTo(ContextUtil.getPackageInfo().versionName) <= 0;
        }

        @Override
        public boolean forceUpdate() {
            //todo return info.force;
            return info.force;
        }

        @Override
        public String getDownloadUrl() {
            return info.download;
//            return "http://cdn.longtugame.com/channel_bin/520006/apk/3.0.7/520006_397.apk";
        }

        @Override
        public void noticeNewest() {
            if (!noticeNewest) {
                return;
            }
            YAlertDialog yAlertDialog = new YAlertDialog(context);
            yAlertDialog.setMessage(context.getString(com.sp.lib.R.string.alread_newest));
            yAlertDialog.show();
        }

        @Override
        public void noticeUpdate() {
            YAlertDialogTwoButton yAlertDialog = new YAlertDialogTwoButton(context);
            yAlertDialog.setTitle(context.getString(R.string.find_new_version));
            yAlertDialog.setMessage(info.description);
            yAlertDialog.setCancelable(false);
            yAlertDialog.setButton1(com.sp.lib.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    doUpdate();
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
            final YAlertDialog dialog = new YAlertDialog(context);
            dialog.setMessage(context.getString(R.string.find_new_version));
            dialog.setCancelable(false);
            dialog.setMessage(info.description);
            dialog.setButton(context.getString(R.string.update_now), new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    doUpdate();
                }
            });

            dialog.show();
        }

        protected void doUpdate() {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            new DownloadTask(context, !forceUpdate()).execute(getDownloadUrl(), dir.getAbsolutePath());
        }

    }

    private static class DownloadTask extends AsyncTask<String, Integer, File> implements DialogInterface.OnDismissListener {
        private YDownloadDialog progressDialog;
        private Context context;
        private Downloader downloader;
        boolean cancelable;

        private DownloadTask(Context context, boolean cancelable) {
            this.context = context;
            this.cancelable = cancelable;
            downloader = new Downloader();

        }

        @Override
        protected void onPreExecute() {
            progressDialog = new YDownloadDialog(context);
            progressDialog.setOnDismissListener(this);
            progressDialog.setCancelable(cancelable);
            if (cancelable) {
                progressDialog.setButton(context.getString(R.string.cancel), new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        progressDialog.dismiss();
                    }
                });
            }
            progressDialog.show();
        }

        @Override
        protected File doInBackground(String... params) {
            String url = params[0];
            String dir = params[1];
            File dirFile = new File(dir);
            return downloader.download(dirFile, url, new Downloader.Callback() {
                @Override
                public void publish(int total, int downloaded) {
                    publishProgress(total, downloaded);
                }

                @Override
                public boolean isCanceled() {
                    return DownloadTask.this.isCancelled();
                }
            });
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int total = values[0] / 1024;
            int downloaded = values[1] / 1024;
            progressDialog.setMax(total);
            progressDialog.setProgress(downloaded);
        }

        @Override
        protected void onPostExecute(final File file) {
            progressDialog.setMessage(context.getString(R.string.download_ok));
            progressDialog.setButton(context.getString(R.string.install_now), new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    installFile(file);
                }
            });
        }

        void installFile(File file) {
            try {
                Uri apk = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(apk, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            cancel(true);
        }
    }
}
