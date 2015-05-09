package com.yjy998.ui.activity.main.my;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.sp.lib.widget.lock.LockView;
import com.sp.lib.widget.lock.NineLock;
import com.yjy998.R;
import com.yjy998.common.Constant;
import com.yjy998.ui.activity.base.SecondActivity;
import com.yjy998.ui.pop.YAlertDialog;

public class SetGesturePassword extends SecondActivity {
    private LockView lockView;
    private TextView lockText;
    private String firstDrawPassword;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        initialize();
    }

    private void initialize() {

        lockView = (LockView) findViewById(R.id.lockView);
        lockText = (TextView) findViewById(R.id.lockText);

        SharedPreferences sp = getSharedPreferences(Constant.PRE_GESTURE, MODE_PRIVATE);
        password = sp.getString(Constant.PRE_GESTURE_PASSWORD, null);
        boolean enabled = sp.getBoolean(Constant.PRE_GESTURE_ENABLED, false);
        if (!TextUtils.isEmpty(password) && enabled) {
            checkPassword();
        } else {
            firstDraw();
        }
    }

    /**
     * 针对已经设置过手势密码的，要先验证手势密码
     */
    private void checkPassword() {
        lockText.setText(getString(R.string.draw_open_gesture_psw));
        lockView.setLock(new CheckLock());
    }

    /**
     * 验证手势密码失败
     */
    private void checkPasswordFailed() {

        SharedPreferences sp = getSharedPreferences(Constant.PRE_GESTURE, MODE_PRIVATE);
        int fails = sp.getInt(Constant.PRE_GESTURE_FAILS, 0);
        fails++;
        sp.edit().putInt(Constant.PRE_GESTURE_FAILS, fails).apply();
        int max = Constant.PRE_GESTURE_MAX_FAILED;
        if (fails >= max) {
            //取消手势密码
            getSharedPreferences(Constant.PRE_GESTURE, MODE_PRIVATE).edit()
                    .putString(Constant.PRE_GESTURE_PASSWORD, null)
                    .putBoolean(Constant.PRE_GESTURE_ENABLED, false)
                    .apply();
            //重新登录
            YAlertDialog dialog = new YAlertDialog(this);
            dialog.setMessage(getString(R.string.password_failed));
            dialog.setButton(getString(R.string.re_login));
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    logout();
                }
            });
            dialog.show();
        } else {
            lockText.setText(getString(R.string.open_lock_failed_d, max - fails));
        }
    }

    /**
     * 设置手势密码第一次绘制
     */
    private void firstDraw() {
        lockText.setText(getString(R.string.draw_gesture_psw));
        lockView.setLock(new FirstLock());
    }

    /**
     * 设置手势密码第二次绘制
     */
    private void secondDraw() {
        lockText.setText(getString(R.string.draw_gesture_psw));
        lockView.setLock(new SecondLock());
    }

    private class CheckLock extends NineLock {
        @Override
        protected boolean tryUnLock() {
            boolean equals = getDrawSecret().equals(getSharedPreferences(Constant.PRE_GESTURE, MODE_PRIVATE).getString(Constant.PRE_GESTURE_PASSWORD, null));
            if (equals) {
                firstDraw();
            } else {
                checkPasswordFailed();
            }
            return equals;
        }
    }

    private class FirstLock extends NineLock {

        @Override
        protected boolean tryUnLock() {
            firstDrawPassword = getDrawSecret();
            secondDraw();
            return true;
        }
    }

    private class SecondLock extends NineLock {

        @Override
        protected boolean tryUnLock() {
            if (getDrawSecret().equals(firstDrawPassword)) {
                getSharedPreferences(Constant.PRE_GESTURE, MODE_PRIVATE).edit()
                        .putString(Constant.PRE_GESTURE_PASSWORD, firstDrawPassword)
                        .putBoolean(Constant.PRE_GESTURE_ENABLED, true)
                        .commit();
                finish();
                return true;
            }
            lockText.setText(getString(R.string.re_draw_gesture));
            return false;
        }
    }
}
