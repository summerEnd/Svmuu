package com.yjy998.ui.activity.main.apply;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yjy998.R;
import com.yjy998.common.entity.Contest;
import com.yjy998.ui.activity.base.SecondActivity;

public class ContestApplyActivity extends SecondActivity {

    private TextView contestName;
    public static final String EXTRA_CONTEST = "contest";
    private Contest mContest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_apply);
        initialize();
    }

    private void initialize() {
        mContest = (Contest) getIntent().getSerializableExtra(EXTRA_CONTEST);
        assert mContest != null;
        contestName = (TextView) findViewById(R.id.contestName);
        contestName.setText(mContest.name);
        ContestApply apply = new ContestApply();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, apply).commit();
    }


    public static class ContestApply extends TNFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            DATA = new int[]{
                    5 * W, 10 * W, 20 * W, 30 * W, 50 * W, 80 * W,
                    100 * W, 150 * W, 200 * W
            };
        }
    }
}
