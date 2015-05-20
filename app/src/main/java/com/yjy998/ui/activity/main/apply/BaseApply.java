package com.yjy998.ui.activity.main.apply;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sp.lib.common.support.net.client.SRequest;
import com.yjy998.R;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.base.BaseFragment;
import com.yjy998.ui.activity.base.YJYActivity;
import com.yjy998.ui.activity.main.more.WebViewActivity;
import com.yjy998.ui.pop.PayDialog;
import com.yjy998.ui.view.CircleItem;


public abstract class BaseApply extends BaseFragment implements View.OnClickListener {


    protected final int W = 10000;
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

        circle[0] = (CircleItem) findViewById(R.id.circle0);
        circle[1] = (CircleItem) findViewById(R.id.circle1);
        circle[2] = (CircleItem) findViewById(R.id.circle2);
        circle[3] = (CircleItem) findViewById(R.id.circle3);
        circle[4] = (CircleItem) findViewById(R.id.circle4);
        circle[5] = (CircleItem) findViewById(R.id.circle5);
        OnCircleClickListener listener = new OnCircleClickListener();
        for (CircleItem item : circle) {
            item.setOnClickListener(listener);
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
        setCircleGroup(currentGroup);
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

                new PayDialog(getActivity(), payAmount.getTag() + "").setCallback(new PayDialog.Callback() {
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

                        YHttpClient.getInstance().post(request, new YHttpHandler() {
                            @Override
                            protected void onStatusCorrect(Response response) {

                            }
                        });
                    }
                }).show();
                break;
            }
            case R.id.questionImage:
            case R.id.introduce: {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra(WebViewActivity.EXTRA_URL, "http://m.yjy998.com/rules.html"));
                break;
            }
        }
    }

    /**
     * 换组金额
     */
    public void changeGroup() {
        currentGroup = (currentGroup + 1) % 2;
        setCircleGroup(currentGroup);
    }

    void setCircleGroup(int group) {
        int index = group * 6;
        for (int i = 0; i < circle.length; i++) {
            Item item = items[i + index];
            if (item == null) {
                circle[i].setVisibility(View.INVISIBLE);
                continue;
            }
            circle[i].setVisibility(View.VISIBLE);

            circle[i].setBoldText(item.boldText);
            circle[i].setNormalText(item.normalText);
            if (item.isSelected) {
                circle[i].setCircle(CircleItem.CIRCLE_RED);
            } else {
                circle[i].setCircle(CircleItem.CIRCLE_BLUE);
            }
        }
    }

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

    public class Item {
        private String normalText;
        private String boldText;
        private int amount;
        private boolean isSelected;
    }

    private class OnCircleClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int index = currentGroup * 6;
            for (int i = 0; i < circle.length; i++) {
                Item item = items[i + index];
                if (item == null) {
                    continue;
                }
                if (circle[i] == v) {
                    item.isSelected = true;
                    setData(item.amount);
                } else {
                    item.isSelected = false;
                }
            }
            setCircleGroup(currentGroup);

        }
    }
}
