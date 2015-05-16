package com.sp.lib.common.admin.check;

import android.widget.TextView;

import java.util.regex.Pattern;

/**
 * Created by user1 on 2015/5/14.
 */
public class PatternCheck extends TextViewCheck {
    String pattern;

    /**
     * 正则检验
     *
     * @param textView   目标TextView
     * @param pattern    匹配的正则表达式
     * @param failNotice 失败文字
     */
    public PatternCheck(TextView textView, String pattern, String failNotice) {
        super(textView, failNotice);
        this.pattern = pattern;
    }


    @Override
    protected boolean onCheck(TextView textView) {
        Pattern p = Pattern.compile(pattern);
        return p.matcher(textView.getText().toString()).matches();
    }
}
