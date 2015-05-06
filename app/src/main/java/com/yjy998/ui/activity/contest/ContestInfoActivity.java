package com.yjy998.ui.activity.contest;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.yjy998.R;
import com.yjy998.entity.Contest;
import com.yjy998.ui.activity.other.SecondActivity;

public class ContestInfoActivity extends SecondActivity {

    private TextView titleText;
    private TextView rankText;
    private TextView rateText;
    private TextView stockNoText;
    private TextView stockNameText;
    private TextView stockValueText;
    private TextView stockHoldingText;
    private TextView applyDate;
    private Button buyIn;
    private Button sellOut;

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
        buyIn = (Button) findViewById(R.id.buyIn);
        sellOut = (Button) findViewById(R.id.sellOut);

        titleText.setText(contest.name);

    }
}
