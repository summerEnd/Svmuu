package com.yjy998.ui.activity.my;

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
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.admin.Assent;
import com.yjy998.admin.User;
import com.yjy998.adapter.ContractPagerAdapter;
import com.yjy998.adapter.ContestPagerAdapter;
import com.yjy998.common.ImageOptions;
import com.yjy998.entity.Contract;
import com.yjy998.entity.Contest;
import com.yjy998.ui.activity.my.business.BusinessActivity;
import com.yjy998.ui.activity.other.BaseFragment;
import com.yjy998.ui.view.TwoTextItem;

import java.util.ArrayList;


public class CenterFragment extends BaseFragment implements View.OnClickListener {

    View layout;
    private ImageView avatarImage;
    private TextView telText;
    private TwoTextItem moneyText;
    private TextView goldIngotText;
    private TextView caopanTicketsText;
    private TextView contractAmount;
    private TextView contestAmount;
    private ViewPager contractPager;
    private ViewPager gamePager;


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
            case R.id.avatarImage: {
                if (AppDelegate.getInstance().isUserLogin()) {
                    startActivity(new Intent(getActivity(), ChangeData.class));
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
                break;
            }
        }
    }

    private void initialize() {
        avatarImage = (ImageView) findViewById(R.id.avatarImage);
        avatarImage.setOnClickListener(this);


        telText = (TextView) findViewById(R.id.telText);
        moneyText = (TwoTextItem) findViewById(R.id.moneyText);
        goldIngotText = (TextView) findViewById(R.id.goldIngotText);
        caopanTicketsText = (TextView) findViewById(R.id.caopanTicketsText);
        contractAmount = (TextView) findViewById(R.id.contractAmount);
        contestAmount = (TextView) findViewById(R.id.contestAmount);
        findViewById(R.id.buyIn).setOnClickListener(this);
        findViewById(R.id.sellOut).setOnClickListener(this);
        findViewById(R.id.recharge).setOnClickListener(this);
        contractPager = (ViewPager) findViewById(R.id.contractPager);
        gamePager = (ViewPager) findViewById(R.id.gamePager);
        refresh();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        refresh();
    }

    public void refresh() {
        if (getView() == null) {
            return;
        }

        if (AppDelegate.getInstance().isUserLogin()) {

            User user = AppDelegate.getInstance().getUser();

            Assent assent = user.assent;
            telText.setText(assent.name);
            moneyText.setText("￥" + assent.avalaible_amount);
            goldIngotText.setText(getString(R.string.GoldIngot_s, assent.yuanbao_total_amount));
            caopanTicketsText.setText(getString(R.string.caopan_s, assent.quan_total_amount));

            ImageLoader.getInstance().displayImage("", avatarImage, ImageOptions.getAvatarInstance());

            ArrayList<Contract> myContracts = AppDelegate.getInstance().getUser().myContracts;
            ArrayList<Contest> myContests = AppDelegate.getInstance().getUser().myContests;
            contestAmount.setText(getString(R.string.myContest_d, myContests != null ? myContests.size() : 0));
            contractAmount.setText(getString(R.string.myContract_d, myContracts != null ? myContracts.size() : 0));
            contractPager.setVisibility(View.VISIBLE);
            gamePager.setVisibility(View.VISIBLE);
            contractPager.setAdapter(new ContractPagerAdapter(myContracts));
            gamePager.setAdapter(new ContestPagerAdapter(myContests));
        } else {
            telText.setText(R.string.userName);
            moneyText.setText("￥0");
            goldIngotText.setText(getString(R.string.GoldIngot_s, "0"));
            caopanTicketsText.setText(getString(R.string.caopan_s, "0"));
            ImageLoader.getInstance().displayImage("", avatarImage, ImageOptions.getAvatarInstance());
            contestAmount.setText(getString(R.string.myContest_d, 0));
            contractAmount.setText(getString(R.string.myContract_d, 0));
            contractPager.setVisibility(View.GONE);
            gamePager.setVisibility(View.GONE);
        }


    }

}
