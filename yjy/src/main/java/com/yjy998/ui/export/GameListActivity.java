package com.yjy998.ui.export;

import android.os.Bundle;

import com.yjy998.R;
import com.yjy998.ui.activity.base.SecondActivity;
import com.yjy998.ui.activity.main.contest.ContestFragment2;

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
