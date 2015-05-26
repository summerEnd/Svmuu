package com.slib.demo;

import android.annotation.TargetApi;
import android.os.Build;
import android.service.dreams.DreamService;

import com.svmuu.slibdemo.R;

public class DreamTest extends SLIBTest {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public class MyDream extends DreamService {

        @Override
        public void onAttachedToWindow() {
            super.onAttachedToWindow();

            // Exit dream upon user touch
            setInteractive(false);
            // Hide system UI
            setFullscreen(true);
            // Set the dream layout
            setContentView(R.layout.dream);
        }
    }

}
