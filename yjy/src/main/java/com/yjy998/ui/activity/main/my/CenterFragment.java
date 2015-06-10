package com.yjy998.ui.activity.main.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.common.entity.Assent;
import com.yjy998.common.entity.User;
import com.yjy998.common.adapter.ContractPagerAdapter;
import com.yjy998.common.adapter.ContestPagerAdapter;
import com.yjy998.common.entity.UserInfo;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.common.util.ImageOptions;
import com.yjy998.common.entity.Contract;
import com.yjy998.common.entity.Contest;
import com.yjy998.ui.activity.admin.LoginDialog;
import com.yjy998.ui.activity.admin.RegisterDialog;
import com.yjy998.ui.activity.main.more.WebViewActivity;
import com.yjy998.ui.activity.main.my.business.BusinessActivity;
import com.yjy998.ui.activity.base.BaseFragment;
import com.yjy998.ui.activity.base.MenuActivity;
import com.yjy998.ui.activity.main.popularize.PopularizeActivity;
import com.yjy998.ui.activity.pay.RechargeActivity;
import com.yjy998.ui.view.TwoTextItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class CenterFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    View layout;
    private ImageView avatarImage;
    private TextView telText;
    private TwoTextItem moneyText;
    private TextView goldIngotText;
    private TextView caopanTicketsText;
    private TextView contractAmount;
    private TextView contestAmount;
    private TextView popularizeAmount;
    private ViewPager contractPager;
    private ViewPager contestPager;
    private TextView noticeText;
    LoginDialog loginDialog;
    RegisterDialog registerDialog;
    View registerText;
    View line;


    View nav_ContractLeft;
    View nav_ContractRight;
    View nav_ContestLeft;
    View nav_ContestRight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_center, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerText: {
                createRegisterDialogIfNeed();
                registerDialog.show();
                break;
            }
            case R.id.telText:
            case R.id.avatarImage: {
                if (AppDelegate.getInstance().isUserLogin()) {
                    startActivity(new Intent(getActivity(), ChangeDataActivity.class));
                } else {
                    createLoginDialogIfNeed();
                    loginDialog.show();
                }
                break;
            }
            case R.id.sellOut: {
                startActivity(new Intent(getActivity(), BusinessActivity.class)
                        .putExtra(BusinessActivity.EXTRA_IS_BUY, false));
                break;
            }
            case R.id.buyIn: {
                startActivity(new Intent(getActivity(), BusinessActivity.class)
                        .putExtra(BusinessActivity.EXTRA_IS_BUY, true));
                break;
            }
            case R.id.recharge: {
                startActivity(new Intent(getActivity(), RechargeActivity.class));
                break;
            }
            case R.id.popularizeAmount:
            case R.id.popImage: {
                startActivity(new Intent(getActivity(), PopularizeActivity.class));
                break;
            }
            case R.id.noticeText: {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra(WebViewActivity.EXTRA_URL, "http://m.yjy998.com/forbid.html"));
                break;
            }

            case R.id.contractLeft:
            case R.id.contractRight:
            case R.id.contestLeft:
            case R.id.contestRight: {
                performPagerGuide(v);
                break;
            }

        }
    }

    void performPagerGuide(View nav) {

        int contestItem = contestPager.getCurrentItem();

        int contractItem = contractPager.getCurrentItem();


        switch (nav.getId()) {
            case R.id.contractLeft: {
                contractItem--;
                break;
            }
            case R.id.contractRight: {
                contractItem++;
                break;
            }
            case R.id.contestLeft: {
                contestItem--;
                break;
            }
            case R.id.contestRight: {
                contestItem++;
                break;
            }
        }

        contestPager.setCurrentItem(contestItem);
        contractPager.setCurrentItem(contractItem);

        onNavChanged();
    }

    void onNavChanged() {

        int contestItem = contestPager.getCurrentItem();
        int totalContest = contestPager.getAdapter().getCount();

        int contractItem = contractPager.getCurrentItem();
        int totalContract = contractPager.getAdapter().getCount();

        nav_ContestLeft.setVisibility(contestItem <= 0 ? View.INVISIBLE : View.VISIBLE);
        nav_ContestRight.setVisibility(contestItem >= totalContest - 1 ? View.INVISIBLE : View.VISIBLE);

        nav_ContractLeft.setVisibility(contractItem <= 0 ? View.INVISIBLE : View.VISIBLE);
        nav_ContractRight.setVisibility(contractItem >= totalContract - 1 ? View.INVISIBLE : View.VISIBLE);
    }


    private void initialize() {
        caopanTicketsText = (TextView) findViewById(R.id.caopanTicketsText);
        popularizeAmount = (TextView) findViewById(R.id.popularizeAmount);
        contractAmount = (TextView) findViewById(R.id.contractAmount);
        goldIngotText = (TextView) findViewById(R.id.goldIngotText);
        contestAmount = (TextView) findViewById(R.id.contestAmount);
        avatarImage = (ImageView) findViewById(R.id.avatarImage);
        moneyText = (TwoTextItem) findViewById(R.id.moneyText);
        noticeText = (TextView) findViewById(R.id.noticeText);
        registerText = findViewById(R.id.registerText);
        telText = (TextView) findViewById(R.id.telText);
        line = findViewById(R.id.line);
        telText.setOnClickListener(this);
        noticeText.setOnClickListener(this);
        avatarImage.setOnClickListener(this);
        registerText.setOnClickListener(this);
        popularizeAmount.setOnClickListener(this);
        findViewById(R.id.buyIn).setOnClickListener(this);
        findViewById(R.id.sellOut).setOnClickListener(this);
        findViewById(R.id.recharge).setOnClickListener(this);
        findViewById(R.id.popImage).setOnClickListener(this);


        (nav_ContestRight = findViewById(R.id.contestRight)).setOnClickListener(this);
        (nav_ContestLeft = findViewById(R.id.contestLeft)).setOnClickListener(this);


        (nav_ContractLeft = findViewById(R.id.contractLeft)).setOnClickListener(this);
        (nav_ContractRight = findViewById(R.id.contractRight)).setOnClickListener(this);


        contractPager = (ViewPager) findViewById(R.id.contractPager);
        contestPager = (ViewPager) findViewById(R.id.gamePager);
        contractPager.addOnPageChangeListener(this);
        contestPager.addOnPageChangeListener(this);
        refresh();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            refresh();
        }
    }

    public void refresh() {
        //先调本地刷新
        refreshUI();

        //再调远程刷新
        SRequest request = new SRequest();

        YHttpClient.getInstance().getByMethod("/h5/account/assentinfo", request, new YHttpHandler(false) {
            @Override
            protected void onStatusCorrect(Response response) {
                AppDelegate.getInstance().setUser(JsonUtil.get(response.data, User.class));
                refreshUI();
            }
        });
    }

    void refreshUI() {
        if (getView() == null) {
            return;
        }
        noticeText.setText(new SimpleDateFormat(getString(R.string.forbidon_format), Locale.getDefault()).format(new Date()));

        if (AppDelegate.getInstance().isUserLogin()) {

            User user = AppDelegate.getInstance().getUser();

            Assent assent = user.assent;
            UserInfo info = user.userInfo;
            telText.setText(info.unick);
            registerText.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            moneyText.setText("￥" + assent.avalaible_amount);
            goldIngotText.setText(getString(R.string.GoldIngot_s, assent.yuanbao_total_amount));
            caopanTicketsText.setText(getString(R.string.caopan_s, assent.quan_total_amount));
            popularizeAmount.setText(getString(R.string.myPopularize_d, user.popularizeAmount));
            ImageLoader.getInstance().displayImage(user.userInfo.uface, avatarImage, ImageOptions.getAvatarInstance());

        } else {
            registerText.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            telText.setText(R.string.login);
            moneyText.setText("￥0");
            goldIngotText.setText(getString(R.string.GoldIngot_s, "0"));
            caopanTicketsText.setText(getString(R.string.caopan_s, "0"));
            ImageLoader.getInstance().displayImage("", avatarImage, ImageOptions.getAvatarInstance());
            contestAmount.setText(getString(R.string.myContest_d, 0));
            contractAmount.setText(getString(R.string.myContract_d, 0));
            popularizeAmount.setText(getString(R.string.myPopularize_d, 0));
        }
        ArrayList<Contract> myContracts = AppDelegate.getInstance().getUser().myContracts;
        ArrayList<Contest> myContests = AppDelegate.getInstance().getUser().myContests;
        contestAmount.setText(getString(R.string.myContest_d, myContests != null ? myContests.size() : 0));
        contractAmount.setText(getString(R.string.myContract_d, myContracts != null ? myContracts.size() : 0));
        contractPager.setAdapter(new ContractPagerAdapter(myContracts));
        contestPager.setAdapter(new ContestPagerAdapter(myContests));
        onNavChanged();
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        onNavChanged();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
