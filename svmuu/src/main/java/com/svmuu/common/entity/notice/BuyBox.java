package com.svmuu.common.entity.notice;

import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;

/**
 * Created by user1 on 2015/7/15.
 * 7.订阅宝盒
 */
public class BuyBox extends BaseSystemNotice {
    public String date;
    public String uid;
    public String uname;
    public String bid;
    public String bname;
    public String btype;

    @Override
    protected CharSequence onCreateContent(Html.ImageGetter getter) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append("感谢")
                .append(new UserNameSpan(uname, uid))
                .append("订阅了").append(btype)
                .append(createClickSpan(bname, new PinkClickSpan()))
        ;
        return sb;
    }
}
