package com.yjy998.ui.activity.main.contest;

import android.os.Bundle;
import android.widget.ListView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.yjy998.R;
import com.yjy998.common.adapter.RankAdapter;
import com.yjy998.common.entity.Rank;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.base.SecondActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RankActivity extends SecondActivity {

    private ListView list;
    private List<Rank> ranks = new ArrayList<Rank>();
    public static final String EXTRA_ID = "id";
    RankAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        initialize();
    }


    private void initialize() {
        adapter = new RankAdapter(this, ranks);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        getRankList();
    }

    public void getRankList() {
        SRequest request = new SRequest("http://mobile.yjy998.com/h5/contest/contestrank");
        request.put("id", getIntent().getStringExtra(EXTRA_ID));
        request.put("limit", 20);
        YHttpClient.getInstance().get(this, request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                try {
                    JSONArray data = new JSONArray(response.data);
                    JsonUtil.getArray(data, Rank.class, adapter.getData());
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
