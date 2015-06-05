package com.yjy998.ui.activity.main.contest;

import android.os.Bundle;
import android.widget.TextView;

import com.yjy998.R;
import com.yjy998.common.entity.Contest;
import com.yjy998.ui.activity.base.SecondActivity;
import com.yjy998.ui.view.RoundButton;

public class ContestInfoActivity extends SecondActivity {

    private TextView titleText;
    private TextView rankText;
    private TextView rateText;
    private TextView stockNoText;
    private TextView stockNameText;
    private TextView stockValueText;
    private TextView stockHoldingText;
    private TextView applyDate;
    private RoundButton buyIn;
    private RoundButton sellOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        initialize();
    }

    private void initialize() {

        Contest contest= (Contest) getIntent().getSerializableExtra("bean");
        if (contest==null){
            return;
        }

        titleText = (TextView) findViewById(R.id.titleText);
        rankText = (TextView) findViewById(R.id.rankText);
        rateText = (TextView) findViewById(R.id.rateText);
        stockNoText = (TextView) findViewById(R.id.stockNoText);
        stockNameText = (TextView) findViewById(R.id.stockNameText);
        stockValueText = (TextView) findViewById(R.id.stockValueText);
        stockHoldingText = (TextView) findViewById(R.id.stockHoldingText);
        applyDate = (TextView) findViewById(R.id.applyDate);
        buyIn = (RoundButton) findViewById(R.id.buyIn);
        sellOut = (RoundButton) findViewById(R.id.sellOut);

        titleText.setText(contest.name);

    }
}
