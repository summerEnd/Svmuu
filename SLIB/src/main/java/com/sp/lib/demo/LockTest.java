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
        LockView lockView = new LockView(getActivity());
        lockView.setLock(new MyLock(""));
        setContentView(lockView);
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
