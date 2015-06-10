package com.yjy998.ui.activity.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sp.lib.common.util.ContextUtil;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.common.entity.Assent;
import com.yjy998.common.entity.User;
import com.yjy998.common.util.ImageOptions;
import com.yjy998.common.util.NumberUtil;
import com.yjy998.ui.activity.admin.About;
import com.yjy998.ui.activity.admin.LoginDialog;
import com.yjy998.ui.activity.admin.RegisterDialog;
import com.yjy998.ui.activity.base.MenuActivity;
import com.yjy998.ui.activity.main.apply.ApplyActivity;
import com.yjy998.ui.activity.main.my.ChangeDataActivity;
import com.yjy998.ui.activity.main.my.business.BusinessActivity;
import com.yjy998.ui.activity.base.BaseFragment;
import com.yjy998.ui.activity.main.popularize.PopularizeActivity;
import com.yjy998.ui.activity.pay.RechargeActivity;

public class MenuFragment extends BaseFragment implements View.OnClickListener {

    private ImageView avatarImage;
    private TextView phoneText;
    private TextView remainMoneyText;
    private TextView goldIngotText;
    private TextView caopanTickets;
    private OnMenuClick menuClick;
    LoginDialog loginDialog;
    RegisterDialog registerDialog;
    View registerText;
    View line;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnMenuClick) {
            menuClick = (OnMenuClick) activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    private void initialize() {

        avatarImage = (ImageView) findViewById(R.id.avatarImage);
        phoneText = (TextView) findViewById(R.id.phoneText);
        remainMoneyText = (TextView) findViewById(R.id.remainMoneyText);
        goldIngotText = (TextView) findViewById(R.id.goldIngotText);
        caopanTickets = (TextView) findViewById(R.id.caopanTickets);
        avatarImage.setOnClickListener(this);
        phoneText.setOnClickListener(this);
        findViewById(R.id.buyIn).setOnClickListener(this);
        findViewById(R.id.sellOut).setOnClickListener(this);
        findViewById(R.id.recharge).setOnClickListener(this);
        findViewById(R.id.applyTn).setOnClickListener(this);
        findViewById(R.id.applyT9).setOnClickListener(this);
        findViewById(R.id.realGame).setOnClickListener(this);
        findViewById(R.id.center).setOnClickListener(this);
        findViewById(R.id.help).setOnClickListener(this);
        findViewById(R.id.about).setOnClickListener(this);
        findViewById(R.id.popularize).setOnClickListener(this);
        registerText = findViewById(R.id.registerText);
        registerText.setOnClickListener(this);
        line = findViewById(R.id.line);
        line.setOnClickListener(this);
        refresh();

    }

    public void refresh() {

        if (getView() == null) {
            return;
        }
        String avatarUrl = "";
        if (AppDelegate.getInstance().isUserLogin()) {
            User user = AppDelegate.getInstance().getUser();
            Assent assent = user.assent;
            line.setVisibility(View.GONE);
            registerText.setVisibility(View.GONE);
            if (assent == null) {
                return;
            }
            phoneText.setText(assent.name);
            remainMoneyText.setText(getString(R.string.remain_money_s, assent.avalaible_amount));
            goldIngotText.setText(getString(R.string.GoldIngot_s, assent.yuanbao_total_amount));
            caopanTickets.setText(getString(R.string.caopan_s, assent.quan_total_amount));
            if (user.userInfo != null) {
                avatarUrl = user.userInfo.uface;
            }
        } else {
            line.setVisibility(View.VISIBLE);
            registerText.setVisibility(View.VISIBLE);
            phoneText.setText(getString(R.string.login));
            remainMoneyText.setText(getString(R.string.remain_money_s, 0));
            goldIngotText.setText(getString(R.string.GoldIngot_s, 0));
            caopanTickets.setText(getString(R.string.caopan_s, 0));

        }
        ImageLoader.getInstance().displayImage(avatarUrl, avatarImage, ImageOptions.getAvatarInstance(getResources().getDimensionPixelOffset(R.dimen.avatarSize)));

    }

    @Override
    public void onClick(View v) {

        if (menuClick != null && menuClick.onMenuClick(v)) {

        } else switch (v.getId()) {
            case R.id.buyIn: {
                startActivity(new Intent(getActivity(), BusinessActivity.class)
                        .putExtra(BusinessActivity.EXTRA_IS_BUY, true));
                break;
            }
            case R.id.sellOut: {

                startActivity(new Intent(getActivity(), BusinessActivity.class)
                        .putExtra(BusinessActivity.EXTRA_IS_BUY, false));
                break;
            }
            case R.id.recharge: {

                if (checkLogin()){
                    return;
                }

                startActivity(new Intent(getActivity(), RechargeActivity.class));
                break;
            }
            case R.id.applyTn: {
                startActivity(new Intent(getActivity(), ApplyActivity.class)
                        .putExtra(ApplyActivity.EXTRA_APPLY, ApplyActivity._TN));
                break;
            }
            case R.id.applyT9: {
                startActivity(new Intent(getActivity(), ApplyActivity.class)
                        .putExtra(ApplyActivity.EXTRA_APPLY, ApplyActivity._T9));
                break;
            }
            case R.id.realGame: {
                startActivity(new Intent(getActivity(), MainActivity.class)
                        .putExtra(MainActivity.EXTRA_CHECK_TAB_ID, MainActivity.ID_GAME));
                break;
            }
            case R.id.center: {
                startActivity(new Intent(getActivity(), MainActivity.class)
                        .putExtra(MainActivity.EXTRA_CHECK_TAB_ID, MainActivity.ID_CENTER));
                break;
            }
            case R.id.help: {
                startActivity(new Intent(getActivity(), MainActivity.class)
                        .putExtra(MainActivity.EXTRA_CHECK_TAB_ID, MainActivity.ID_MORE));
                break;
            }
            case R.id.about: {
                startActivity(new Intent(getActivity(), About.class));
                break;
            }
            case R.id.phoneText: {
                if (!AppDelegate.getInstance().isUserLogin()) {

                    createLoginDialogIfNeed();
                    loginDialog.show();
                } else {
                    startActivity(new Intent(getActivity(), ChangeDataActivity.class));
                }
                break;
            }
            case R.id.registerText: {
                if (!AppDelegate.getInstance().isUserLogin()) {

                    createRegisterDialogIfNeed();
                    registerDialog.show();
                }
                break;
            }
            case R.id.avatarImage: {
                if (!checkLogin()) {
                    return;
                }
                startActivity(new Intent(getActivity(), ChangeDataActivity.class));
                break;
            }
            case R.id.popularize: {
                startActivity(new Intent(getActivity(), PopularizeActivity.class));
                break;
            }
        }
//        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private boolean checkLogin() {
        if (!AppDelegate.getInstance().isUserLogin()) {
            showLoginDialog();
            return false;
        }
        return true;
    }

    private void createLoginDialogIfNeed() {

        if (loginDialog != null) {
            return;
        }

        loginDialog = new LoginDialog(getActivity());
        loginDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((MenuActivity) getActivity()).refreshTitle();
                ((MenuActivity) getActivity()).refreshLayout();
            }
        });
    }

    private void createRegisterDialogIfNeed() {

        if (registerDialog != null) {
            return;
        }

        registerDialog = new RegisterDialog(getActivity());

    }

    void showLoginDialog() {
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).showLoginWindow();
            ContextUtil.toast(getString(R.string.please_login_first));
        }
    }

    public interface OnMenuClick {

        public boolean onMenuClick(View v);
    }
}
