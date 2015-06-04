package com.yjy998.ui.activity.base;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;

import com.yjy998.R;
import com.yjy998.ui.activity.base.YJYActivity;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 二级页面
 */
public class SecondActivity extends YJYActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();

        if (actionBar != null) {
            ImageView logo = new ImageView(this);
            logo.setImageResource(R.drawable.logo_white);
            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            actionBar.setCustomView(logo, lp);
            setTitle("   ");
        }
    }

}
