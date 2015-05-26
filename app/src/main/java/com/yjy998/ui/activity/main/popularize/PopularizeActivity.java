package com.yjy998.ui.activity.main.popularize;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.widget.list.refresh.PullToRefreshBase;
import com.yjy998.R;
import com.yjy998.common.adapter.PopularizeAdapter;
import com.yjy998.common.adapter.PopularizeFriendsAdapter;
import com.yjy998.common.entity.Popularize;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.base.SecondActivity;
import com.yjy998.ui.view.number.GrowNumber;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PopularizeActivity extends SecondActivity implements PullToRefreshBase.OnRefreshListener, PopularizeView.OnSelectedListener {

    private PopularizeView content;
    private ListView listView;
    PopularizeAdapter adapterFlow;
    PopularizeFriendsAdapter adapterInvite;
    private boolean doRefresh = false;
    private int flowPage = 0;
    private int invitePage = 0;
    private boolean isFlow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popularize);
        initialize();
    }

    private void initialize() {
        content = (PopularizeView) findViewById(R.id.content);
        listView = content.getListView();
        adapterFlow = new PopularizeAdapter(this, new ArrayList<Popularize>());
        adapterInvite = new PopularizeFriendsAdapter(this, new ArrayList<Popularize>());
        listView.setAdapter(adapterFlow);
        content.setOnRefreshListener(this);
        content.listener = this;
        getFlowList(0);

    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        if (isFlow) {
            getFlowList(0);
        } else {
            getInviteList(0);
        }
        doRefresh = true;

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (isFlow) {
            getFlowList(flowPage + 1);
        } else {
            getInviteList(invitePage + 1);
        }
    }

    public void getInviteList(int pn) {
        SRequest request = new SRequest("http://mobile.yjy998.com/h5/plan/queryInvited");
        request.put("pn", pn);
        YHttpClient.getInstance().post(request, new YHttpHandler(false) {
            @Override
            protected void onStatusCorrect(Response response) {
                try {

                    confirmAdapter(adapterInvite, adapterInvite.getData(), response.data);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                if (doRefresh) {
                    adapterInvite.getData().clear();
                    invitePage = 0;
                } else {
                    invitePage++;
                }
                doRefresh = false;
                content.refreshListView.onPullDownRefreshComplete();
                content.refreshListView.onPullUpRefreshComplete();
            }
        });
    }

    /**
     * 获取返利流水列表
     */
    public void getFlowList(int pn) {
        SRequest request = new SRequest("http://mobile.yjy998.com/h5/plan/queryFortune");
        request.put("pn", pn);
        YHttpClient.getInstance().post(request, new YHttpHandler(false) {
            @Override
            protected void onStatusCorrect(Response response) {
                try {

                    confirmAdapter(adapterFlow, adapterFlow.getData(), response.data);

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                if (doRefresh) {
                    adapterFlow.getData().clear();
                    flowPage = 0;
                } else {
                    flowPage++;
                }

                doRefresh = false;
                content.refreshListView.onPullDownRefreshComplete();
                content.refreshListView.onPullUpRefreshComplete();
            }
        });
    }

    void confirmAdapter(BaseAdapter adapter, List<Popularize> dataList, String jsonStr) {
        try {
            JSONObject data = new JSONObject(jsonStr);
            JsonUtil.getArray(data.getJSONArray("inviterList"), Popularize.class, dataList);
            content.invitePeople.setText(getString(R.string.total_s_people, data.getString("inviterNumber")));
            startAnim(data.getString("profitAmount"));
        } catch (JSONException e) {
        }

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter instanceof HeaderViewListAdapter) {
            listAdapter = ((HeaderViewListAdapter) listAdapter).getWrappedAdapter();
        }
        if (listAdapter != adapter) {
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }


    }

    void startAnim(String profit) {
        AnimatorSet set = new AnimatorSet();
        GrowText target = new GrowText();
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "Value", 0, Float.parseFloat(profit));
        animator.setDuration(700);

        ObjectAnimator scale = ObjectAnimator.ofFloat(target, "Scale", 1, 1.4f, 1);
        scale.setDuration(300);
        set.playSequentially(animator, scale);

        set.start();
    }

    /**
     * 测试用例见{@link com.yjy998.common.test.Test#testFormat()} }
     */
    void printFormat(String profit) {
        int floatPart;//小数部分
        int intPart;//整数部分
        try {
            BigDecimal decimal = new BigDecimal(profit);
            intPart = decimal.intValue();
            floatPart = (int) ((decimal.floatValue() - intPart) * 100);
        } catch (NumberFormatException e) {
            intPart = 0;
            floatPart = 0;
        }
        content.integerText.setText("" + intPart);
        content.floatText.setText(String.format(".%02d", floatPart));
    }

    @Override
    public void onSelected(int i) {
        doRefresh = false;
        isFlow = i == 0;
        if (isFlow) {//返利流水

            if (adapterFlow.getData().size() == 0) {
                getFlowList(0);
            } else {
                confirmAdapter(adapterFlow, adapterFlow.getData(), "");
            }
        } else {//邀请好友

            if (adapterInvite.getData().size() == 0) {
                getInviteList(0);
            } else {
                confirmAdapter(adapterInvite, adapterInvite.getData(), "");
            }
        }

    }

    private class GrowText extends GrowNumber {
        @Override
        public void setValue(float number) {
            printFormat(number + "");
        }

        public void setScale(float value) {
            View parent = (View) content.floatText.getParent();
            parent.setScaleX(value);
            parent.setScaleY(value);
        }
    }


}
