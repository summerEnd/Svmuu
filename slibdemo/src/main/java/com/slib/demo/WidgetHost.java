package com.slib.demo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.sp.lib.common.util.ContextUtil;
import com.svmuu.slibdemo.R;

public class WidgetHost extends SLIBTest {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        {
            Button button = new Button(this);
            layout.addView(button);
            button.setText("rotate");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


    public static class MyWidget extends AppWidgetProvider {
        public MyWidget() {
        }

        String broadCastString = "hello Lincoln!";
        int index;
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(broadCastString)) {
                //只能通过远程对象来设置appwidget中的控件状态
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.provider);

                //通过远程对象将按钮的文字设置为”hihi”
                remoteViews.setTextViewText(R.id.text, "lincon"+index++);

                //获得appwidget管理实例，用于管理appwidget以便进行更新操作
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

                //相当于获得所有本程序创建的appwidget
                ComponentName componentName = new ComponentName(context, MyWidget.class);

                //更新appwidget
                appWidgetManager.updateAppWidget(componentName, remoteViews);
            }
            super.onReceive(context, intent);
            ContextUtil.toast("onReceive");
        }

        @Override
        public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
            super.onUpdate(context, appWidgetManager, appWidgetIds);
            ContextUtil.toast("onUpdate");
            Intent intent = new Intent();
            intent.setAction(broadCastString);


            //设置pendingIntent的作用
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.provider);

            //绑定事件
            remoteViews.setOnClickPendingIntent(R.id.providerImg1, pendingIntent);
            remoteViews.setOnClickPendingIntent(R.id.providerImg2, pendingIntent);

            //更新Appwidget
            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        }
    }
}
