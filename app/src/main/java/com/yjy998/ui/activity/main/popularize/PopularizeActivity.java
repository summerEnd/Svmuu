package com.yjy998.ui.activity.main.popularize;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.common.util.SLog;
import com.sp.lib.widget.list.refresh.PullToRefreshBase;
import com.yjy998.R;
import com.yjy998.common.adapter.PopularizeAdapter;
import com.yjy998.common.entity.Popularize;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.common.util.NumberUtil;
import com.yjy998.ui.activity.base.SecondActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PopularizeActivity extends SecondActivity implements PullToRefreshBase.OnRefreshListener {

    private PopularizeView content;
    private ListView listView;
    PopularizeAdapter adapter;
    private boolean doRefresh = false;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popularize);
        initialize();
    }

    private void initialize() {
        content = (PopularizeView) findViewById(R.id.content);
        listView = content.getListView();
        adapter = new PopularizeAdapter(this, new ArrayList<Popularize>());
        listView.setAdapter(adapter);
        content.setOnRefreshListener(this);
        getPopularizeList(0);
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        getPopularizeList(page + 1);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        doRefresh = true;
        getPopularizeList(0);
    }

    public void getPopularizeList(int pn) {
        SRequest request = new SRequest("http://mobile.yjy998.com/h5/plan/myinviters");
        request.put("pn", pn);
        YHttpClient.getInstance().post(request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                try {
                    if (doRefresh) {
                        adapter.getData().clear();
                        page = 0;
                    } else {
                        page++;
                    }
                    JSONObject data = new JSONObject(response.data);
                    JsonUtil.getArray(data.getJSONArray("inviterList"), Popularize.class, adapter.getData());
                    adapter.notifyDataSetChanged();
                    content.invitePeople.setText(getString(R.string.total_s_people, data.getString("inviterNumber")));
                    String profit = data.getString("profitAmount");
                    printFormat(profit);
                } catch (NumberFormatException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                doRefresh = false;
                content.refreshListView.onPullDownRefreshComplete();
                content.refreshListView.onPullDownRefreshComplete();
            }
        });
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


}
