package com.sp.lib.common.admin;


import android.content.Context;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.sp.lib.R;
import com.sp.lib.common.util.ContextUtil;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * 对TextView中的text进行检验
 */
public class AdminManager {
    private Context context;
    private ArrayList<Check> checks = new ArrayList<Check>();

    public AdminManager(Context context) {
        this.context = context;
    }

    /**
     * 对textView字符的长度进行检验
     *
     * @param tv         目标TextView
     * @param minLength  长度最小值
     * @param maxLength  长度最大值
     * @param failNotice 失败文字
     */
    public AdminManager addLengthCheck(TextView tv, int minLength, int maxLength, String failNotice) {
        checks.add(new LengthCheck(tv, minLength, maxLength, failNotice));
        return this;
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
     * @see AdminManager.EmptyCheck
     */
    public AdminManager addEmptyCheck(TextView textView, String failNotice) {
        checks.add(new EmptyCheck(textView, failNotice));
        return this;
    }

    /**
     * 对两个textView字符是否相等进行检验
     *
     * @param textView   目标TextView，如果检测不通过，这个textView将产生动画效果
     * @param another    另一个TextView
     * @param failNotice 失败文字
     */
    public AdminManager addEqualCheck(TextView textView, TextView another, String failNotice) {
        checks.add(new EqualCheck(textView, another, failNotice));
        return this;
    }


    public AdminManager addPatterCheck(TextView tv, String pattern, String failNotice) {
        checks.add(new PatterCheck(tv, pattern, failNotice));
        return this;
    }

    public boolean start() {
        for (Check check : checks) {
            if (!check.doCheck()) {
                return false;
            }
        }
        return true;
    }

    private interface Check {
        public boolean doCheck();
    }

    private abstract class TextCheck implements Check {
        protected TextView textView;
        protected Animation shake;
        private String failNotice;

        public TextCheck(TextView textView) {
            this(textView, textView.getHint().toString());
        }

        private TextCheck(TextView textView, String failNotice) {
            this.textView = textView;
            this.failNotice = failNotice;
            shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        }

        @Override
        public final boolean doCheck() {
            if (!onCheck(textView)) {
                textView.startAnimation(shake);
                textView.requestFocus();
                ContextUtil.toast(failNotice);
                return false;
            }
            return true;
        }

        protected abstract boolean onCheck(TextView textView);
    }


    /**
     * 是否为空测试
     */
    private class EmptyCheck extends TextCheck {


        private EmptyCheck(TextView textView, String failNotice) {
            super(textView, failNotice);
        }

        @Override
        protected boolean onCheck(TextView textView) {
            return !TextUtils.isEmpty(textView.getText().toString());
        }
    }

    private class LengthCheck extends TextCheck {
        int start;
        int end;

        /**
         * 对textView字符的长度进行检验
         *
         * @param textView 目标TextView
         * @param start    长度最小值
         * @param end      长度最大值
         */
        private LengthCheck(TextView textView, int start, int end, String failNotice) {
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

    private class EqualCheck extends TextCheck {
        TextView another;

        /**
         * 对两个textView字符是否相等进行检验
         *
         * @param textView   目标TextView，如果检测不通过，这个textView将产生动画效果
         * @param another    另一个TextView
         * @param failNotice 失败文字
         */
        private EqualCheck(TextView textView, TextView another, String failNotice) {
            super(textView, failNotice);
            this.another = another;
        }

        @Override
        protected boolean onCheck(TextView textView) {
            return textView.getText().toString().equals(another.getText().toString());
        }
    }

    public class PatterCheck extends TextCheck {
        String pattern;

        /**
         * 正则检验
         *
         * @param textView   目标TextView
         * @param pattern    匹配的正则表达式
         * @param failNotice 失败文字
         */
        public PatterCheck(TextView textView, String pattern, String failNotice) {
            super(textView, failNotice);
            this.pattern = pattern;
        }


        @Override
        protected boolean onCheck(TextView textView) {
            Pattern p = Pattern.compile(pattern);
            return p.matcher(textView.getText().toString()).matches();
        }
    }
}
