//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.svmuu.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;

import com.gensee.chat.gif.GifDrawalbe;
import com.gensee.chat.gif.GifHelper;
import com.gensee.utils.ThreadPool;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GifDrawable extends AnimationDrawable {
    public static double ration = 1.3333333333333333D;
    private static final int READ_FRAME = 1000;
    private Context context;
    private AtomicBoolean bParsing = new AtomicBoolean(false);
    GifHelper helper;
    private List<GifDrawalbe.UpdateUIListen> updateUIListenList;
    private Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            if (((Boolean) msg.obj).booleanValue()) {
                GifDrawable.this.start();
            }

            return false;
        }
    });
    InputStream stream;

    public boolean isParsing() {
        return this.bParsing.get();
    }

    public GifDrawable(Context context, InputStream stream) {
        this.stream = stream;
        this.context = context;
        this.bParsing.set(false);
        this.updateUIListenList = new ArrayList();
        this.setOneShot(false);
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream, new Rect(), opts);
        this.setBounds(0, 0, (int) ((double) opts.outWidth * ration), (int) ((double) opts.outHeight * ration));
        this.invalidateSelf();
    }

    public GifDrawable(Context context, int id) {
        this(context, context.getResources().openRawResource(id));

    }

    public void readFrames(final boolean bStarted) {
        if (!this.bParsing.get()) {
            ThreadPool.getInstance().execute(new Runnable() {
                public void run() {
                    GifDrawable.this.parseFrame();
                    Message msg = new Message();
                    msg.obj = Boolean.valueOf(bStarted);
                    msg.what = 1000;
                    GifDrawable.this.handler.sendMessage(msg);
                }
            });
        } else {
            this.start();
        }

    }

    private void parseFrame() {
        synchronized (this) {
            if (!this.bParsing.get()) {
                GifHelper helper = new GifHelper();
                helper.read(stream);
                int gifCount = helper.getFrameCount();
                if (gifCount <= 0) {
                    this.bParsing.set(false);
                    return;
                }

                BitmapDrawable bd = new BitmapDrawable(this.context.getResources(), helper.getImage());
                this.addFrame(bd, helper.getDelay(0));

                for (int i = 1; i < helper.getFrameCount(); ++i) {
                    this.addFrame(new BitmapDrawable((Resources) null, helper.nextBitmap()), helper.getDelay(i));
                }

                this.bParsing.set(true);
            }

        }
    }

    public void run() {
        super.run();
        this.invalidateSelf();
        synchronized (this) {
            Iterator var3 = this.updateUIListenList.iterator();

            while (var3.hasNext()) {
                GifDrawalbe.UpdateUIListen listen = (GifDrawalbe.UpdateUIListen) var3.next();
                listen.updateUI();
            }

        }
    }

    public void addListen(GifDrawalbe.UpdateUIListen listen) {
        synchronized (this) {
            if (!this.updateUIListenList.contains(listen)) {
                this.updateUIListenList.add(listen);
            }

        }
    }

    public void removeListen(GifDrawalbe.UpdateUIListen listen) {
        synchronized (this) {
            this.updateUIListenList.remove(listen);
        }
    }

    public void scheduleSelf(Runnable what, long when) {
        synchronized (this) {
            if (this.updateUIListenList.size() <= 0) {
                this.stop();
                return;
            }
        }

        this.handler.postAtTime(what, when);
    }

    public interface UpdateUIListen {
        void updateUI();
    }
}
