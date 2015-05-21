package com.yjy998.ui.activity.main.popularize;

import android.os.Bundle;
import android.widget.ListView;

import com.yjy998.R;
import com.yjy998.common.adapter.PopularizeAdapter;
import com.yjy998.common.entity.Popularize;
import com.yjy998.ui.activity.base.SecondActivity;

import java.util.ArrayList;

public class PopularizeActivity extends SecondActivity {

    private PopularizeView content;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popularize);
        initialize();
    }

    private void initialize() {
        content = (PopularizeView) findViewById(R.id.content);
        listView = content.getListView();
        PopularizeAdapter adapter = new PopularizeAdapter(this, new ArrayList<Popularize>());
        adapter.setTestCount(30);
        listView.setAdapter(adapter);
    }


}
