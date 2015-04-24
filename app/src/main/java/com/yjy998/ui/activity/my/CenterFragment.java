package com.yjy998.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sp.lib.widget.list.LinearListView;
import com.yjy998.R;
import com.yjy998.adapter.ContractAdapter;
import com.yjy998.adapter.GameAdapter;
import com.yjy998.common.ImageOptions;
import com.yjy998.entity.Contract;
import com.yjy998.entity.Game;
import com.yjy998.ui.activity.other.BaseFragment;
import com.yjy998.ui.activity.other.ChangeData;
import com.yjy998.ui.view.TwoTextItem;

import java.util.ArrayList;


public class CenterFragment extends BaseFragment implements View.OnClickListener {

    View layout;
    private ImageView avatarImage;
    private TextView telText;
    private TwoTextItem moneyText;
    private TextView goldIngotText;
    private TextView caopanTicketsText;
    private TextView buyIn;
    private TextView sellOut;
    private TextView recharge;
    private LinearListView contractLayout;
    private LinearListView gameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_home_logined, container, false);
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
        buyIn = (TextView) findViewById(R.id.buyIn);
        sellOut = (TextView) findViewById(R.id.sellOut);
        recharge = (TextView) findViewById(R.id.recharge);
        contractLayout = (LinearListView) findViewById(R.id.contractLayout);
        gameLayout = (LinearListView) findViewById(R.id.gameLayout);

        ArrayList<Contract> arrayList = new ArrayList<Contract>();
        ArrayList<Game> gameList = new ArrayList<Game>();
        for (int i = 0; i < 15; i++) {
            arrayList.add(new Contract());
            gameList.add(new Game());
        }
        contractLayout.setAdapter(new ContractAdapter(getActivity(), arrayList));
        gameLayout.setAdapter(new GameAdapter(getActivity(), gameList));
    }
}
