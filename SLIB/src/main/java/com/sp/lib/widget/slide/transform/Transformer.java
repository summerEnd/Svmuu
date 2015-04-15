package com.sp.lib.widget.slide.transform;

import android.view.View;

/**
 * Created by user1 on 2015/4/14.
 */
public interface Transformer {
    /**
     * @param delta value from 0 to 1,0 is closed ,1 is open
     */
    public void transform(View main, View menu, float delta);
}
