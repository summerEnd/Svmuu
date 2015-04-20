package com.sp.lib.common.util.time;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public abstract class TimeTicker {


    Timer mTimer;

    final int maxTime;

    private Handler handler;

    private int curTime;

    protected TimeTicker(int maxTime) {
        this.maxTime = maxTime;
        curTime = maxTime;
        handler = new Handler(Looper.getMainLooper(), new CountDownHandler());
    }

    public void start() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        curTime = maxTime;
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 0, 1000);
    }

    public abstract void onTick(int timeRemain);

    protected void onFinish() {

    }

    private class CountDownHandler implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            curTime--;
            if (curTime <= 0) {
                mTimer.cancel();
                onFinish();
            } else {
                onTick(curTime);
            }

            return false;
        }
    }


}
