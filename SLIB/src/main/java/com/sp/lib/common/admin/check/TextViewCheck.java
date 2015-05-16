package com.sp.lib.common.admin.check;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.sp.lib.R;
import com.sp.lib.common.util.ContextUtil;

public abstract class TextViewCheck implements ICheck{
    protected TextView textView;
    protected Animation shake;
    private String failNotice;

    public TextViewCheck(TextView textView) {
        this(textView, textView.getHint().toString());
    }

    public TextViewCheck(TextView textView, String failNotice) {
        this.textView = textView;
        this.failNotice = failNotice;
    }

    @Override
    public final boolean doCheck() {
        if (!onCheck(textView)) {
            if (shake==null){
                shake = AnimationUtils.loadAnimation(textView.getContext(), R.anim.shake);
            }
            textView.startAnimation(shake);
            textView.requestFocus();
            ContextUtil.toast(failNotice);
            return false;
        }
        return true;
    }

    protected abstract boolean onCheck(TextView textView);
}
