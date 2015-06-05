package com.yjy998.ui.export;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.yjy998.R;
import com.yjy998.common.adapter.ContestListAdapter;
import com.yjy998.common.entity.Contest;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.base.BaseListFragment;
import com.yjy998.ui.activity.base.SecondActivity;
import com.yjy998.ui.activity.main.apply.ContestApplyActivity;
import com.yjy998.ui.activity.main.contest.ContestFragment2;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class GameListActivity extends SecondActivity{
    ContestFragment2 contest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        contest=new ContestFragment2();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer,contest).commit();

    }


}
