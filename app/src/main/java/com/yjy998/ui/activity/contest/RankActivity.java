package com.yjy998.ui.activity.contest;

import android.os.Bundle;
import android.widget.ListView;

import com.yjy998.R;
import com.yjy998.adapter.RankAdapter;
import com.yjy998.entity.Rank;
import com.yjy998.ui.activity.other.SecondActivity;

import java.util.ArrayList;
import java.util.List;

public class RankActivity extends SecondActivity {

    private ListView list;
    private List<Rank> ranks = new ArrayList<Rank>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        initialize();
    }


    private void initialize() {
        RankAdapter adapter = new RankAdapter(this, ranks);
        adapter.setTestCount(12);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
    }
}
