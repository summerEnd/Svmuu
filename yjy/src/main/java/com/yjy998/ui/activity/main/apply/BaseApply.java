package com.yjy998.ui.activity.main.apply;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.ContextUtil;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.base.BaseFragment;
import com.yjy998.ui.activity.base.YJYActivity;
import com.yjy998.ui.activity.main.more.WebViewActivity;
import com.yjy998.ui.activity.pay.PayDialog;
import com.yjy998.ui.pop.YAlertDialog;
import com.yjy998.ui.view.CircleItem;


public abstract class BaseApply extends BaseFragment implements View.OnClickListener {


    protected final int W = 10000;
    //金额
    protected int[] DATA = new int[]{
            5000, W, 3 * W, 5 * W, 10 * W, 20 * W,
            30 * W, 50 * W, 80 * W, 100 * W, 150 * W, 200 * W
    };
    private Item[] items = new Item[12];
    private CircleItem[] circle = new CircleItem[6];
    private int currentGroup = 0;
    private TextView totalCapitalText;
    private TextView manageFeeText;
    private TextView pingCangText;
    private TextView keepText;
    private TextView payAmount;
    private TextView pingCangSummary;
    int total = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_apply_t, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initialize();
    }


    private void initialize() {


        int[] ids = new int[]{R.id.circle0, R.id.circle1, R.id.circle2, R.id.circle3, R.id.circle4, R.id.circle5};
        OnCircleClickListener listener = new OnCircleClickListener();

        for (int i = 0; i < ids.length; i++) {
            circle[i] = (CircleItem) findViewById(ids[i]);
            circle[i].setOnClickListener(listener);//为每一个圈圈增加监听
        }


        findViewById(R.id.changeGroup).setOnClickListener(this);
        findViewById(R.id.introduce).setOnClickListener(this);
        findViewById(R.id.payNow).setOnClickListener(this);
        totalCapitalText = (TextView) findViewById(R.id.totalCapitalText);
        manageFeeText = (TextView) findViewById(R.id.manageFeeText);
        pingCangSummary = (TextView) findViewById(R.id.pingCangSummary);
        findViewById(R.id.questionImage).setOnClickListener(this);
        pingCangText = (TextView) findViewById(R.id.pingCangText);
        keepText = (TextView) findViewById(R.id.keepText);
        payAmount = (TextView) findViewById(R.id.payAmount);

        //初始化数据
        int dataLength = DATA.length;
        for (int i = 0; i < items.length; i++) {

            if (i >= dataLength) {
                break;
            }
            items[i] = new Item();
            items[i].amount = DATA[i];
            if (DATA[i] < W) {
                items[i].boldText = "";
                items[i].normalText = DATA[i] + getString(R.string.yuan);
            } else {
                items[i].boldText = DATA[i] / W + "";
                items[i].normalText = getString(R.string.ten_thousand);
            }
        }
        invalidateCircleText(currentGroup);
        setData(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeGroup: {
                changeGroup();
                break;
            }
            case R.id.payNow: {
                if (getActivity() instanceof YJYActivity) {
                    if (((YJYActivity) getActivity()).showLoginDialogIfNeed()) {
                        return;
                    }
                }

                final PayDialog payDialog = new PayDialog(getActivity(), payAmount.getTag() + "");
                payDialog.setCallback(new PayDialog.Callback() {
                    @Override
                    public void onPay(String password, String rsa_password) {
                        SRequest request = new SRequest("http://www.yjy998.com/contract/apply");
                        request.put("apply_type", getType());//TN或T9
                        request.put("deposit_amount", total);//总金额
                        request.put("pay_pwd", password);//支付密码
                        request.put("prev_store", 1);
                        request.put("pro_id", getPro_id());
                        request.put("pro_term", "2");
                        request.put("trade_pwd", rsa_password);//交易密码
                        request.put("source_param", "android");//交易密码

                        YHttpClient.getInstance().post(request, new YHttpHandler() {
                            @Override
                            protected void onStatusCorrect(Response response) {
                                payDialog.dismiss();
                                AppDelegate.getInstance().refreshUserInfo();
                            }

                            @Override
                            protected void onStatusFailed(Response response) {
                                super.onStatusFailed(response);
                                YAlertDialog.show(getActivity(), response.message);
                            }
                        });
                    }
                }).show();
                break;
            }
            case R.id.questionImage: {
                ContextUtil.toast(R.string.manage_fee_introduce);
                break;
            }

            case R.id.introduce: {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra(WebViewActivity.EXTRA_URL, getIntroduceUrl()));
                break;
            }
        }
    }

    /**
     * 换组金额
     */
    public void changeGroup() {
        currentGroup = (currentGroup + 1) % 2;
        invalidateCircleText(currentGroup);
    }

    /**
     * 刷新圆圈的颜色和文本
     *
     * @param group 0或者1
     */
    void invalidateCircleText(int group) {
        int startIndex = group * 6;
        for (int i = 0; i < circle.length; i++) {
            Item item = items[i + startIndex];
            if (item == null) {
                circle[i].setVisibility(View.INVISIBLE);
                continue;
            }
            circle[i].setVisibility(View.VISIBLE);
            circle[i].setTag(item);

            circle[i].setBoldText(item.boldText);
            circle[i].setNormalText(item.normalText);

            if (item.isSelected) {
                circle[i].setCircle(CircleItem.CIRCLE_RED);
            } else {
                circle[i].setCircle(CircleItem.CIRCLE_BLUE);
            }
        }
    }

    /**
     * 在这里计算各种金额
     *
     * @param total 所选金额
     */
    void setData(int total) {
        this.total = total;
        float fee = total * 0.098f / 100f;//管理费：按天收取，周末节假日免费
        float keep = getKeep(total);
        float rate = getRate(total);
        float pingCang = keep * rate + total - keep;
        float pay = getFeeDays() * fee + keep;

        totalCapitalText.setText(total + "");
        manageFeeText.setText(fee + "");
        keepText.setText(keep + "");
        pingCangText.setText(pingCang + "");
        payAmount.setText("￥" + pay);
        payAmount.setTag(pay);
        pingCangSummary.setText(getString(R.string.pingCangSummary_f, rate));

    }


    /**
     * 平仓线 = 操盘保证金 * rate+ 配资金额
     */
    public abstract float getRate(int total);

    /**
     * 操盘保证金
     */
    public abstract float getKeep(int total);

    /**
     * 获取初始账户管理费需要交的天数
     */
    public abstract int getFeeDays();

    /**
     * @return TN或T9
     */
    public abstract String getType();

    /**
     * 获取产品id
     */
    public abstract String getPro_id();

    /**
     * 玩法介绍链接
     *
     * @return
     */
    public abstract String getIntroduceUrl();

    public class Item {
        private String normalText;
        private String boldText;
        private int amount;
        private boolean isSelected;
    }

    private class OnCircleClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //清除选中状态
            for (Item item : items) {
                if (item != null) {
                    item.isSelected = false;
                }
            }

            //设置选中效果
            Item selected = (Item) v.getTag();
            if (selected != null) {
                selected.isSelected = true;
                setData(selected.amount);
            }

            invalidateCircleText(currentGroup);

        }
    }
}
