package com.sp.lib.demo;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sp.lib.R;
import com.sp.lib.support.NotificationUtil;

public class NotificationTest extends SlibDemoWrapper {
    public NotificationTest(SlibDemo demo) {
        super(demo, "NOTIFICATION", "test notification features.");
    }

    @Override
    protected void onCreate() {
        SlibDemo context = getActivity();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        {
            Button button = new Button(context);
            button.setText("send");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    PendingIntent intent = PendingIntent.getActivity(context, 100, new Intent(context, SlibDemo.class), PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationUtil.notify(v.getContext(), intent, R.drawable.ic_launcher, "Content Info", "title", "contentText");
                }
            });
            layout.addView(button);
        }

        {
            Button button = new Button(context);
            button.setText("send progress");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotificationUtil.notifyProgress(v.getContext());
                }
            });
            layout.addView(button);
        }
        setContentView(layout);
    }
}
