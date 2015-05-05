package com.sp.lib.common.support.update;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import com.sp.lib.R;
import com.sp.lib.common.util.ContextUtil;

import java.util.ArrayList;

public abstract class UpdateHandler implements UpdateManager.Callback {


    Context context;
    private ProgressDialog dialog;
    private Downloader downloader;

    public UpdateHandler(Context context) {
        this.context = context;
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMax(100);
        downloader = new Downloader();
    }

    @Override
    public void noticeNewest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.alread_newest));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void noticeUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.alread_newest));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doUpdate();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void noticeForceUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.alread_newest));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doUpdate();
            }
        });

        builder.show();
    }

    protected void doUpdate() {
        long id = downloader.downloadWithNotification(getDownloadUrl(), null, null, "hello", "hello");
        context.getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, new MyObserver(new Handler(), id));
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