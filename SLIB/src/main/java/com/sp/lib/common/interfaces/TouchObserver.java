package com.sp.lib.common.interfaces;

import android.view.MotionEvent;

public interface TouchObserver {
    /**
     * @return true 拦截触屏事件
     */
    public boolean dispatch(MotionEvent event);

    /**
     * @return false 不监听触屏事件
     */
    public boolean isEnabled();
}
