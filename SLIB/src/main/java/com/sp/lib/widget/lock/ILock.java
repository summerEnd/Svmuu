package com.sp.lib.widget.lock;

import android.graphics.drawable.Drawable;

public interface ILock {
    public void unLock();

    public void lock();

    public boolean compare();
}
