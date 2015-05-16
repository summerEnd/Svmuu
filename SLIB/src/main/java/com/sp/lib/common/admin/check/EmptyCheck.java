package com.sp.lib.common.admin.check;

import android.text.TextUtils;
import android.widget.TextView;

/**
 * Created by user1 on 2015/5/14.
 */
public class EmptyCheck extends TextViewCheck {

    public EmptyCheck(TextView textView, String failNotice) {
        super(textView, failNotice);
    }

    @Override
    protected boolean onCheck(TextView textView) {
        return !TextUtils.isEmpty(textView.getText().toString());
    }
}
