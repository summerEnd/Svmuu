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
import com.yjy998.R;
import com.yjy998.adapter.ContractPagerAdapter;
import com.yjy998.adapter.GamePagerAdapter;
import com.yjy998.common.ImageOptions;
import com.yjy998.entity.Contract;
import com.yjy998.entity.Game;
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
                startActivity(new Intent(getActivity(), ChangeData.class));
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
        ImageLoader.getInstance().displayImage("", avatarImage, ImageOptions.getAvatarInstance());

        telText = (TextView) findViewById(R.id.telText);
        moneyText = (TwoTextItem) findViewById(R.id.moneyText);
        goldIngotText = (TextView) findViewById(R.id.goldIngotText);
        caopanTicketsText = (TextView) findViewById(R.id.caopanTicketsText);
        findViewById(R.id.buyIn).setOnClickListener(this);
        findViewById(R.id.sellOut).setOnClickListener(this);
        findViewById(R.id.recharge).setOnClickListener(this);
        contractPager = (ViewPager) findViewById(R.id.contractPager);
        gamePager = (ViewPager) findViewById(R.id.gamePager);

        ArrayList<Contract> arrayList = new ArrayList<Contract>();
        ArrayList<Game> gameList = new ArrayList<Game>();
        for (int i = 0; i < 7; i++) {
            arrayList.add(new Contract());
            gameList.add(new Game());
        }
        contractPager.setAdapter(new ContractPagerAdapter(arrayList));
        gamePager.setAdapter(new GamePagerAdapter(gameList));
    }

}
