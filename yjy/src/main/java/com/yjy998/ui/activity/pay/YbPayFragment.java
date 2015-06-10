package com.yjy998.ui.activity.pay;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sp.lib.common.util.ContextUtil;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.common.entity.User;
import com.yjy998.common.entity.UserInfo;
import com.yjy998.ui.activity.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class YbPayFragment extends BaseFragment {


    private TextView accountText;
    private EditText amoutEdit;
    private Button confirmBtn;

    @Override
    public String getTitle() {
        return ContextUtil.getString(R.string.onlinePay_yb);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_yb_pay, container, false);
    }


    private void initialize() {

        accountText = (TextView) findViewById(R.id.accountText);
        amoutEdit = (EditText) findViewById(R.id.amoutEdit);
        confirmBtn = (Button) findViewById(R.id.confirmBtn);
    }

    @Override
    public void refresh() {
        User user = AppDelegate.getInstance().getUser();
        UserInfo info = user.userInfo;
        accountText.setText(info.unick);
    }
}
