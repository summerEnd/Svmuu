package com.svmuu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sp.lib.common.support.cache.CacheManager;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.svmuu.R;
import com.svmuu.common.adapter.BaseHolder;
import com.svmuu.common.adapter.decoration.DividerDecoration;
import com.svmuu.common.adapter.search.SearchAdapter;
import com.svmuu.common.entity.History;
import com.svmuu.common.entity.Search;
import com.svmuu.common.http.HttpHandler;
import com.svmuu.common.http.HttpManager;
import com.svmuu.common.http.Response;
import com.svmuu.ui.BaseActivity;
import com.svmuu.ui.activity.live.LiveActivity;
import com.svmuu.ui.widget.CustomSearchView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity implements CustomSearchView.Callback {


    public static final String SEARCH_HISTORIES = "search_histories";
    private CustomSearchView searchView;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    ArrayList<History> histories;
    ArrayList<Search> searches = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initialize();
    }

    @SuppressWarnings("unchecked")
    private void initialize() {
        try {
            histories = (ArrayList<History>) CacheManager.getInstance().read(SEARCH_HISTORIES);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (histories == null) {
                histories = new ArrayList<>();
            }
        }

        searchView = (CustomSearchView) findViewById(R.id.searchView);
        findViewById(R.id.cancel_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        searchView.setCallback(this);
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        adapter = new SearchAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerDecoration(this));
        adapter.showHistory(histories);
        adapter.setListener(new BaseHolder.OnItemListener() {
            @Override
            public void onClick(View itemView, int position) {
                if (adapter.isShowHistory()) {
                    if (histories == null) {
                        return;
                    }

                    int clearPosition = histories.size() + 1;
                    if (position == clearPosition) {
                        histories.clear();
                        CacheManager.getInstance().write(SEARCH_HISTORIES,histories);
                        adapter.notifyDataSetChanged();
                    } else {
                        search(histories.get(position - 1).name);
                    }
                } else {
                    String uid = searches.get(position).uid;
                    addToHistory(uid);
                    startActivity(new Intent(SearchActivity.this, LiveActivity.class)
                                    .putExtra(LiveActivity.EXTRA_QUANZHU_ID, uid)
                    );
                }
            }
        });

    }

    @Override
    public void onSearch(String key) {
        addToHistory(key);
        search(key);
    }

    @Override
    public void onEdit(String key) {
        search(key);
    }

    @Override
    public void onJump() {

    }

    private void addToHistory(String key) {
        History history = new History();
        history.name = key;
        if (histories.contains(history)) {
            histories.remove(history);
        }
        //将搜索记录移动到最新
        histories.add(0, history);
        CacheManager.getInstance().write(SEARCH_HISTORIES, histories);
    }

    public void search(String key) {
        SRequest request = new SRequest("find");
        request.put("kw", key);
        HttpManager.getInstance().postMobileApi(this, request, new HttpHandler() {
            @Override
            public void onResultOk(int statusCOde, Header[] headers, Response response) throws JSONException {
                searches.clear();
                JsonUtil.getArray(new JSONArray(response.data), Search.class, searches);
                adapter.showResult(searches);
                searchView.onSearchComplete();

            }
        });
    }

}
