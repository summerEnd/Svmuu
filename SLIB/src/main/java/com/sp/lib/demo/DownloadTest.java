package com.sp.lib.demo;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sp.lib.R;
import com.sp.lib.common.support.adapter.ViewHolderAdapter;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.support.update.Downloader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DownloadTest extends SlibDemoWrapper {

    private static final String DESCRIPTION = "Test multiple thread download with DownloadManager";

    public DownloadTest(SlibDemo demo) {
        super(demo, "DownloadTest", DESCRIPTION);
    }

    DownloadReceiver receiver;
    MyObserver observer;
    static List<Long> ids = new ArrayList<Long>();
    List<Progress> tasks = new LinkedList<Progress>();
    MyAdapter adapter;
    float CELL = 1024 * 1024;
    ListView listView;


    @Override
    public void onCreate() {
        listView = new ListView(getActivity());
        setContentView(listView);
        adapter = new MyAdapter(tasks);
        listView.setAdapter(adapter);
        receiver = new DownloadReceiver();
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_VIEW_DOWNLOADS);
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, intentFilter);
        observer = new MyObserver(new Handler());

        getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, observer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download, menu);
        return true;
    }

    void invalidate() {

        DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        long[] filterIds = new long[ids.size()];
        for (int i = 0; i < filterIds.length; i++) {
            filterIds[i] = ids.get(i);
        }
        if (filterIds.length <= 0)
            return;

        Cursor c = manager.query(new DownloadManager.Query().setFilterById(filterIds));

        int _id = c.getColumnIndex(DownloadManager.COLUMN_ID);
        int _status = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int _down = c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
        int _total = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
        tasks.clear();
        adapter.notifyDataSetChanged();
        while (c.moveToNext()) {
            long id = c.getLong(_id);
            int status = c.getInt(_status);
            long down = c.getLong(_down);
            long total = c.getLong(_total);
            Progress p = new Progress();

            float fixedDown = down / CELL;
            float fixedTotal = total / CELL;

            p.progress = (int) ((fixedDown / fixedTotal) * 100);
            p.state = String.format("%s:(%.2fM/%.2fM)", getStatus(status), fixedDown, fixedTotal);
            p.id = id;
            tasks.add(p);
        }
        c.close();
        adapter.notifyDataSetChanged();
    }

    String getStatus(int status) {
        String result = null;
        switch (status) {
            case DownloadManager.STATUS_FAILED:
                result = "下载失败";
                break;
            case DownloadManager.STATUS_PAUSED:
                result = "暂停";
                break;
            case DownloadManager.STATUS_PENDING:
                result = "挂起";
                break;
            case DownloadManager.STATUS_RUNNING:
                result = "下载中";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                result = "下载成功";
                break;
        }
        return result;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        try {
            registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_VIEW_DOWNLOADS));
            getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, observer);
            invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(receiver);
            getContentResolver().unregisterContentObserver(observer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == R.id.download) {
            long id = new Downloader().downloadWithNotification("http://cdn.longtugame.com/channel_bin/520006/apk/3.0.7/520006_397.apk", null, "520006_397.apk", "520006_397.apk", "520006_397.apk");
            ids.add(id);
            invalidate();
            return true;
        } else if (i == R.id.check) {
            startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
            return true;
        }
        return false;
    }


    class MyObserver extends ContentObserver {
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyObserver(Handler handler) {
            super(handler);
        }


        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            invalidate();
        }
    }

    class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ContextUtil.toast("接受了");
        }
    }

    class MyAdapter extends ViewHolderAdapter<Progress, ViewHolder> {


        public MyAdapter(List<Progress> data) {
            super(getActivity(), data, R.layout.progress);
        }

        @Override
        public ViewHolder doFindIds(View convertView) {
            ViewHolder holder = new ViewHolder();
            holder.progress = (ProgressBar) convertView.findViewById(R.id.progressBar);
            holder.tv_state = (TextView) convertView.findViewById(R.id.textView);
            holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);

            return holder;
        }

        @Override
        public void displayView(View v, ViewHolder holder, int position) {
            Progress data = getItem(position);
            holder.progress.setProgress(data.progress);
            holder.tv_state.setText(data.state);
            holder.tv_id.setText("id:" + data.id);
        }
    }

    private class ViewHolder {
        TextView tv_state;
        TextView tv_id;
        ProgressBar progress;
    }

    class Progress {
        String state;
        int progress;
        long id;
    }
}
