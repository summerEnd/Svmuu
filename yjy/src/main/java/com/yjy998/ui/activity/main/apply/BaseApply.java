package com.yjy998.ui.activity.main.apply;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.JsonUtil;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.common.entity.Deposit;
import com.yjy998.common.entity.RateConfig;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.base.BaseFragment;
import com.yjy998.ui.activity.base.YJYActivity;
import com.yjy998.ui.activity.main.more.WebViewActivity;
import com.yjy998.ui.activity.pay.PayDialog;
import com.yjy998.ui.pop.YAlertDialog;
import com.yjy998.ui.pop.YProgressDialog;
import com.yjy998.ui.view.CircleItem;

import java.math.BigDecimal;
import java.util.ArrayList;


public abstract class BaseApply extends BaseFragment implements View.OnClickListener {


    public static final int W = 10000;
    public static final String RATIO_4 = "4";
    public static final String RATIO_9 = "9";
    //金额
    private ProductConfig[] productConfigs = new ProductConfig[12];
    private CircleItem[] circle = new CircleItem[6];
    private int currentGroup = 0;
    private TextView totalCapitalText;
    private TextView manageFeeText;
    private TextView pingCangText;
    private TextView keepText;
    private TextView payAmount;
    private TextView pingCangSummary;

    CapitalConfig configs;
    String type = RATIO_4;//默认选择4
    ProductConfig selected;//所选配资
    //杠杆变化监听
    private RadioGroup.OnCheckedChangeListener leverChange = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rate_4: {
                    type = RATIO_4;
                    break;
                }
                case R.id.rate_9: {
                    type = RATIO_9;
                    break;
                }
            }
            setData(selected);
            invalidateCircleText(currentGroup);
        }
    };

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
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(leverChange);

        //初始化数据
        setData(null);
        getCapitalList();
    }

    public void getCapitalList() {
        SRequest request = new SRequest("http://mobile.yjy998.com/h5/contest/getprodconfig");
        request.put("prod_id", getPro_id());
        YHttpClient.getInstance().post(request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                configs = JsonUtil.get(response.data, CapitalConfig.class);
                initData();
            }

            @Override
            protected void onStatusFailed(Response response) {
                super.onStatusFailed(response);
                initData();
            }

            @Override
            public Dialog onCreateDialog() {
                if (getActivity() == null) {
                    return null;
                }
                return new YProgressDialog(getActivity());
            }
        });
    }

    void initData() {


        if (configs == null || configs.productConfig == null) {
            return;
        }

        productConfigs = new ProductConfig[configs.productConfig.size()];


        for (int i = 0; i < productConfigs.length; i++) {

            productConfigs[i] = configs.productConfig.get(i);
            int quota = productConfigs[i].quota;
            if (quota < W) {
                this.productConfigs[i].boldText = "";
                this.productConfigs[i].normalText = quota + getString(R.string.yuan);
            } else {
                this.productConfigs[i].boldText = quota / W + "";
                this.productConfigs[i].normalText = getString(R.string.ten_thousand);
            }
        }
        invalidateCircleText(currentGroup);
        circle[0].performClick();
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
                onApplyPressed();
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

    //点击我要操盘的回调
    protected void onApplyPressed() {
        startPay();
    }

    /**
     * 开始调取接口支付
     */
    public void startPay() {

        if (selected == null) {
            ContextUtil.toast(getString(R.string.select_capital));
            return;
        }

        final PayDialog payDialog = new PayDialog(getActivity(), payAmount.getTag() + "");
        payDialog.setCallback(new PayDialog.Callback() {
            @Override
            public void onPay(String password, String rsa_password) {
                SRequest request = new SRequest("http://www.yjy998.com/contract/apply");
                request.put("apply_type", type);//TN或T9
                request.put("deposit_amount", selected.quota);//总金额
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

                    @Override
                    public Dialog onCreateDialog() {

                        if (getActivity()==null)return null;

                        return new YProgressDialog(getActivity());
                    }
                });
            }
        }).show();
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
            ProductConfig productConfig = productConfigs[i + startIndex];
            if (productConfig == null) {
                circle[i].setVisibility(View.INVISIBLE);
                continue;
            }
            circle[i].setVisibility(View.VISIBLE);
            circle[i].setTag(productConfig);

            circle[i].setBoldText(productConfig.boldText);
            circle[i].setNormalText(productConfig.normalText);

            if (productConfig.isSelected) {
                circle[i].setCircle(CircleItem.CIRCLE_RED);
            } else {
                circle[i].setCircle(CircleItem.CIRCLE_BLUE);
            }
            if (RATIO_4.equals(type)) {
                circle[i].setIsDrawCover(productConfig.enabled);
            }
            productConfig.enabled = type.equals(RATIO_4) ? productConfig.lever4 : productConfig.lever9;
            circle[i].setIsDrawCover(!productConfig.enabled);
            circle[i].invalidate();
        }
    }

    /**
     * 在这里计算各种金额
     *
     * @param deposit 所选金额
     */
    void setData(ProductConfig deposit) {
        selected = deposit;
        int total=0;

        if (deposit != null) {
            if (type.equals(RATIO_4) ? deposit.lever4 : deposit.lever9) {
                total = deposit.quota;
                deposit.isSelected=true;
            }else{
                deposit.isSelected=false;
            }
        }
        RateConfig rateConfig = null;
        if (configs != null && configs.ratioConfig != null) {
            for (RateConfig config : configs.ratioConfig) {
                if (config.ratio_type.equals(type)) {
                    rateConfig = config;
                    break;
                }
            }
        }

        if (rateConfig == null) {
            rateConfig = new RateConfig();
        }

        float fee = new BigDecimal(rateConfig.rate).divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_EVEN).floatValue() * total;//管理费：按天收取，周末节假日免费
        float keep = total * rateConfig.ratio;
        float rate = type.equals(RATIO_4) ? 0.45f : 0.5f;//9:0.5 4:0.45
        float pingCang = keep * rate + total - keep;
        float pay = rateConfig.days * fee + keep;

        totalCapitalText.setText(total + "");
        manageFeeText.setText(fee + "");
        keepText.setText(keep + "");
        pingCangText.setText(pingCang + "");
        payAmount.setText("￥" + pay);
        payAmount.setTag(pay);
        pingCangSummary.setText(getString(R.string.pingCangSummary_f, rate));

    }


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
     */
    public abstract String getIntroduceUrl();

    public class ProductConfig extends Deposit {
        private String normalText;
        private String boldText;
        private boolean isSelected;
        private boolean enabled;
    }

    public class CapitalConfig {
        ArrayList<ProductConfig> productConfig;
        ArrayList<RateConfig> ratioConfig;
    }

    private class OnCircleClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            CircleItem circleItem = (CircleItem) v;
            if (circleItem.isDrawCover()) {
                return;
            }
            //清除选中状态
            for (ProductConfig item : productConfigs) {
                if (item != null) {
                    item.isSelected = false;
                }
            }

            //设置选中效果
            ProductConfig selected = (ProductConfig) v.getTag();
            if (selected != null) {
                setData(selected);
            }

            invalidateCircleText(currentGroup);

        }
    }
}
