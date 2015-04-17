package com.sp.lib.demo;

import com.sp.lib.R;
import com.sp.lib.widget.lock.NineLock;
import com.sp.lib.widget.lock.LockView;

public class LockTest extends SlibDemoWrapper {
    protected LockTest(SlibDemo demo) {
        super(demo, "LockView", "reset test");
    }

    @Override
    protected void onCreate() {
        setContentView(R.layout.sample_slock_view);
        LockView lockView = (LockView) findViewById(R.id.lock);
        lockView.setLock(new MyLock(""));
    }

    private class MyLock extends NineLock {

        public MyLock(String secret) {
            super(secret);
        }

        @Override
        public boolean tryUnLock() {
            boolean equals = "3459".equals(getSecret().toString());
            reset();
            return equals;
        }
    }
}
