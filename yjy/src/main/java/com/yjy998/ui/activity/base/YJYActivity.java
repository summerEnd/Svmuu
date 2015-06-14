package com.yjy998.ui.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.sp.lib.activity.SlibActivity;
import com.sp.lib.common.util.ContextUtil;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.admin.LoginDialog;
import com.yjy998.ui.activity.admin.LoginRegisterWindow;
import com.yjy998.ui.activity.main.MainActivity;
import com.yjy998.ui.activity.admin.LockActivity;

/**
 * 所有Activity的基类
 */
public class YJYActivity extends SlibActivity implements View.OnClickListener {
    private LoginRegisterWindow mLoginWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!AppDelegate.getInstance().didAppRunForeground()) {
            AppDelegate.getInstance().setEnterBackground(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isLogined", AppDelegate.getInstance().isUserLogin());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState!=null){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppDelegate.getInstance().isUserLogin() && AppDelegate.getInstance().shouldLockScreen() && LockActivity.isLockEnabled()) {
            startActivity(new Intent(this, LockActivity.class));
        }
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 退出登录
     */
    protected void logout() {
        YHttpClient.getInstance().logout(new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                AppDelegate.getInstance().logout();
                Intent intent = new Intent(YJYActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    /**
     * 检查用户是否已经登录
     */
    public boolean checkLogin() {
        return AppDelegate.getInstance().isUserLogin();
    }

    public boolean showLoginDialogIfNeed() {
        boolean need = !checkLogin();
        if (need) {
            ContextUtil.toast(R.string.please_login_first);
            if (mLoginWindow == null) {
                mLoginWindow = new LoginRegisterWindow(this);
                mLoginWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        refreshLayout();
                    }
                });
            }
            mLoginWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }
        return need;
    }

    /**
     * 登陆状态发生改变时要刷新一下布局
     */
    public void refreshLayout() {

    }


}
