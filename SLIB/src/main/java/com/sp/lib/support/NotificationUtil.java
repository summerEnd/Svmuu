package com.sp.lib.support;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.sp.lib.R;
import com.sp.lib.demo.SlibDemo;

public class NotificationUtil {
    public static void notify(Context context, PendingIntent intent, int icon, CharSequence contentInfo, CharSequence title, CharSequence contentText) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentInfo(contentInfo)
                .setContentIntent(intent)
                .setContentTitle(title)
                .setSmallIcon(icon)
                .setContentText(contentText)
                .setAutoCancel(false)
        ;
        manager.notify(R.id.NotifyCationId_1, builder.build());
    }

    public static void notifyProgress(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent intent = PendingIntent.getActivity(context, 100, new Intent(context, SlibDemo.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentInfo("Content Info")
                .setContentIntent(intent)
                .setContentTitle("title")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText("contentText")
                .setAutoCancel(false)
                .setProgress(100, 90, false);
        ;
        manager.notify(R.id.NotifyCationId_2, builder.build());
    }
}
