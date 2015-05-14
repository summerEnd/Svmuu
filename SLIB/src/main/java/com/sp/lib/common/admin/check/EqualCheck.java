package com.sp.lib.common.admin.check;

import android.widget.TextView;

/**
 * Created by user1 on 2015/5/14.
 */
public class EqualCheck
        extends TextViewCheck {
    TextView another;

    /**
     * 对两个textView字符是否相等进行检验
     *
     * @param textView   目标TextView，如果检测不通过，这个textView将产生动画效果
     * @param another    另一个TextView
     * @param failNotice 失败文字
     */
    public EqualCheck(TextView textView, TextView another, String failNotice) {
        super(textView, failNotice);
        this.another = another;
    }

    @Override
    protected boolean onCheck(TextView textView) {
        return textView.getText().toString().equals(another.getText().toString());
    }
}
