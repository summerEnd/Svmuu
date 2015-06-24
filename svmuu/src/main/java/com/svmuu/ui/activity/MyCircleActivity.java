package com.svmuu.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sp.lib.common.util.ContextUtil;
import com.svmuu.R;
import com.svmuu.ui.BaseActivity;
import com.svmuu.ui.activity.live.LiveListFragment;
import com.svmuu.ui.widget.SelectBar;

public class MyCircleActivity extends BaseActivity implements SelectBar.OnSelectListener{
    SelectBar selectBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_list);
        selectBar= (SelectBar) findViewById(R.id.selectedBar);
        selectBar.setListener(this);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer,new LiveListFragment()).commit();
    }


    @Override
    public void onSelect(int index) {
        ContextUtil.toast(index);
        TextView first = (TextView) selectBar.getChildAt(0);
        TextView second = (TextView) selectBar.getChildAt(1);
        if (index==0){
            first.setTextColor(selectBar.getCOLOR_NORMAL());
            second.setTextColor(selectBar.getCOLOR_CHECKED());
        }else{
            first.setTextColor(selectBar.getCOLOR_CHECKED());
            second.setTextColor(selectBar.getCOLOR_NORMAL());
        }

    }
}
