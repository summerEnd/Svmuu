package com.yjy998.ui.activity.main.apply;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.yjy998.R;
import com.yjy998.common.Constant;
import com.yjy998.common.entity.Contest;
import com.yjy998.ui.activity.base.SecondActivity;
import com.yjy998.ui.pop.TokenDialog;

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
        apply.isElite = "11".equals(mContest.id);
        apply.proId = mContest.id;
        int W = BaseApply.W;

        if ("34".equals(mContest.id)) {
            apply.DATA = new int[]{
                    5 * W,10 * W, 20 * W, 30 * W, 50 * W, 80 * W,
                    100 * W, 150 * W, 200 * W
            };
        }else{
            apply.DATA = new int[]{
                   10 * W, 20 * W, 30 * W, 50 * W, 80 * W,
                    100 * W, 150 * W, 200 * W
            };
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, apply).commit();
    }

    public static class ContestApply extends TNApplyFragment {

        private TokenDialog tokenDialog;
        public boolean isElite = false;
        public String proId;

        @Override
        protected void onApplyPressed() {
            if (isElite) {
                showTokenDialog();
            } else {
                startPay();
            }
        }

        private void showTokenDialog() {
            if (tokenDialog == null) {
                tokenDialog = new TokenDialog(getActivity());
                tokenDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (tokenDialog.isTokenRight()) {
                            startPay();
                        }
                    }
                });
            }
            tokenDialog.show();
        }

        @Override
        public String getPro_id() {
            return proId;
        }
    }
}
