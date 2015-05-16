package com.sp.lib.common.admin.check;

import android.widget.TextView;

/**
 * Created by user1 on 2015/5/14.
 */
public class LengthCheck extends TextViewCheck {
    int start;
    int end;

    /**
     * 对textView字符的长度进行检验
     *
     * @param textView 目标TextView
     * @param start    长度最小值
     * @param end      长度最大值
     */
    public LengthCheck(TextView textView, int start, int end, String failNotice) {
        super(textView, failNotice);
        this.start = start;
        this.end = end;
    }

    @Override
    protected boolean onCheck(TextView textView) {
        String text = textView.getText().toString();
        return text.length() >= start && text.length() <= end;
    }
}
