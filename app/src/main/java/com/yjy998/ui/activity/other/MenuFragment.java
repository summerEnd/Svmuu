package com.yjy998.ui.activity.other;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yjy998.R;
import com.yjy998.common.ImageOptions;
import com.yjy998.ui.activity.MainActivity;
import com.yjy998.ui.activity.apply.ApplyActivity;
import com.yjy998.ui.activity.game.CapitalActivity;

public class MenuFragment extends BaseFragment implements View.OnClickListener {

    private ImageView avatarImage;
    private TextView phoneText;
    private TextView remainMoneyText;
    private TextView goldIngotText;
    private TextView discountTickets;
    private OnMenuClick menuClick;

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
        View inflate = inflater.inflate(R.layout.fragment_main_menu, null);
        return inflate;
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
        discountTickets = (TextView) findViewById(R.id.discountTickets);
        findViewById(R.id.buyIn).setOnClickListener(this);
        findViewById(R.id.sellOut).setOnClickListener(this);
        findViewById(R.id.recharge).setOnClickListener(this);
        findViewById(R.id.applyTn).setOnClickListener(this);
        findViewById(R.id.applyT9).setOnClickListener(this);
        findViewById(R.id.realGame).setOnClickListener(this);
        findViewById(R.id.center).setOnClickListener(this);
        findViewById(R.id.help).setOnClickListener(this);
        ImageLoader.getInstance().displayImage("", avatarImage, ImageOptions.getAvatarInstance(getResources().getDimensionPixelOffset(R.dimen.avatarSize)));

    }

    @Override
    public void onClick(View v) {

        if (menuClick != null && menuClick.onMenuClick(v)) {

        } else switch (v.getId()) {
            case R.id.buyIn: {
                startActivity(new Intent(getActivity(), CapitalActivity.class)
                        .putExtra(CapitalActivity.EXTRA_IS_BUY, true));
                break;
            }
            case R.id.sellOut: {

                startActivity(new Intent(getActivity(), CapitalActivity.class)
                        .putExtra(CapitalActivity.EXTRA_IS_BUY, false));
                break;
            }
            case R.id.recharge: {
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
        }
        getActivity().overridePendingTransition(R.anim.slide_up_in, R.anim.stand_still);
    }

    public interface OnMenuClick {

        public boolean onMenuClick(View v);
    }
}
