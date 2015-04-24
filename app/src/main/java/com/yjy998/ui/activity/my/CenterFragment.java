package com.yjy998.ui.activity.my;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sp.lib.common.support.adapter.GuidePagerAdapter;
import com.yjy998.R;
import com.yjy998.common.ImageOptions;
import com.yjy998.entity.Contract;
import com.yjy998.entity.Game;
import com.sp.lib.common.interfaces.TouchObserver;
import com.sp.lib.common.interfaces.TouchDispatcher;
import com.yjy998.ui.activity.other.BaseFragment;
import com.yjy998.ui.activity.other.ChangeData;
import com.yjy998.ui.view.TwoTextItem;

import java.util.ArrayList;


public class CenterFragment extends BaseFragment implements View.OnClickListener, TouchObserver {

    View layout;
    private ImageView avatarImage;
    private TextView telText;
    private TwoTextItem moneyText;
    private TextView goldIngotText;
    private TextView caopanTicketsText;
    private TextView buyIn;
    private TextView sellOut;
    private TextView recharge;
    private ViewPager contractPager;
    private ViewPager gamePager;
    private Rect contractRectF;
    private Rect gameRectF;
    final int PAGE_CONTRACT = 1;
    final int PAGE_GAME = 2;
    private int handlePage = 0;
    private int[] offset = new int[]{-1, -1};

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof TouchDispatcher) {
            ((TouchDispatcher) activity).register(this);
        }
    }


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
        contractPager = (ViewPager) findViewById(R.id.contractPager);
        gamePager = (ViewPager) findViewById(R.id.gamePager);

        ArrayList<Contract> arrayList = new ArrayList<Contract>();
        ArrayList<Game> gameList = new ArrayList<Game>();
        for (int i = 0; i < 15; i++) {
            arrayList.add(new Contract());
            gameList.add(new Game());
        }
        contractPager.setAdapter(new GuidePagerAdapter(getActivity(), new int[]{R.layout.item_contract, R.layout.item_contract, R.layout.item_contract}));
        gamePager.setAdapter(new GuidePagerAdapter(getActivity(), new int[]{R.layout.item_game, R.layout.item_game, R.layout.item_game}));
    }

    @Override
    public boolean dispatch(MotionEvent event) {

        if (contractRectF == null) {
            contractRectF = new Rect();
        }
        if (gameRectF == null) {
            gameRectF = new Rect();
        }
        contractPager.getGlobalVisibleRect(contractRectF);
        gamePager.getGlobalVisibleRect(gameRectF);


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                int y = (int) event.getY();
                int x = (int) event.getX();
                if (contractRectF.contains(x, y)) {
                    contractPager.dispatchTouchEvent(event);
                    handlePage = PAGE_CONTRACT;
                    return true;
                }
                if (gameRectF.contains(x, y)) {
                    gamePager.dispatchTouchEvent(event);
                    handlePage = PAGE_GAME;
                    return true;
                }
                handlePage = 0;
            }
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP: {
                dispatchTouchToPager(event);
                break;
            }
        }

        return false;
    }

    void dispatchTouchToPager(MotionEvent event) {
        switch (handlePage) {
            case 0:
                break;
            case PAGE_GAME:
                gamePager.dispatchTouchEvent(event);
                break;
            case PAGE_CONTRACT:
                contractPager.dispatchTouchEvent(event);
                break;
        }
    }

    @Override
    public boolean isEnabled() {
        return isVisible();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() instanceof TouchDispatcher) {
            ((TouchDispatcher) getActivity()).unRegister(this);
        }
    }
}
