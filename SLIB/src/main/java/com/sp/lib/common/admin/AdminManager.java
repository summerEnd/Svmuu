package com.sp.lib.common.admin;


import android.widget.TextView;

import com.sp.lib.common.admin.check.EmptyCheck;
import com.sp.lib.common.admin.check.EqualCheck;
import com.sp.lib.common.admin.check.ICheck;
import com.sp.lib.common.admin.check.LengthCheck;
import com.sp.lib.common.admin.check.PatternCheck;

import java.util.ArrayList;

/**
 * 对TextView中的text进行检验
 */
public class AdminManager {
    private ArrayList<ICheck> checks = new ArrayList<ICheck>();

    /**
     * 对textView字符的长度进行检验
     *
     * @param tv         目标TextView
     * @param minLength  长度最小值
     * @param maxLength  长度最大值
     * @param failNotice 失败文字
     */
    public AdminManager addLengthCheck(TextView tv, int minLength, int maxLength, String failNotice) {
        return add(new LengthCheck(tv, minLength, maxLength, failNotice));
    }

    /**
     * 与{@link AdminManager#addEmptyCheck(android.widget.TextView, String) addEmptyCheck(TextView,String)}相同，用TextView的hint作为failNotice
     *
     * @see AdminManager#addEmptyCheck(android.widget.TextView, String)
     */
    public AdminManager addEmptyCheck(TextView textView) {
        return addEmptyCheck(textView, textView.getHint().toString());
    }

    /**
     * @see com.sp.lib.common.admin.check.EmptyCheck
     */
    public AdminManager addEmptyCheck(TextView textView, String failNotice) {
        return add(new EmptyCheck(textView, failNotice));

    }

    /**
     * 对两个textView字符是否相等进行检验
     *
     * @param textView   目标TextView，如果检测不通过，这个textView将产生动画效果
     * @param another    另一个TextView
     * @param failNotice 失败文字
     */
    public AdminManager addEqualCheck(TextView textView, TextView another, String failNotice) {

        return add(new EqualCheck(textView, another, failNotice));
    }


    public AdminManager addPatterCheck(TextView tv, String pattern, String failNotice) {
        return add(new PatternCheck(tv, pattern, failNotice));
    }

    public AdminManager add(ICheck check) {
        checks.add(check);
        return this;
    }

    /**
     * @return true 测试通过,false 不通过
     */
    public boolean start() {
        for (ICheck check : checks) {
            if (!check.doCheck()) {
                return false;
            }
        }
        return true;
    }


}
