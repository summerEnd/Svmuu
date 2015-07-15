package com.svmuu.common.entity.notice;

import android.text.Html;
import android.text.SpannableStringBuilder;

/**
 * Created by user1 on 2015/7/15.
 * 创建文字宝盒
 */
public class CreateTextBox extends BaseSystemNotice {
    public String date;
    public String uid;
    public String uname;
    public String bid;
    public String bname;
    public String btype;

    @Override
    protected CharSequence onCreateContent(Html.ImageGetter getter) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(new UserNameSpan(uname, uid));
        sb.append("刚刚创建了");
        sb.append(btype);
        sb.append(createClickSpan(bname, new PinkClickSpan()));

        return sb;
    }
}
