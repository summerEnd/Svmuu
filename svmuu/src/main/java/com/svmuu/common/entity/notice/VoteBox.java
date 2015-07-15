package com.svmuu.common.entity.notice;

import android.text.Html;
import android.text.SpannableStringBuilder;

/**
 * Created by user1 on 2015/7/15.
 * 投票
 */
public class VoteBox extends BaseSystemNotice {
    public String date;
    public String uid;
    public String uname;
    public String num;

    @Override
    protected CharSequence onCreateContent(Html.ImageGetter getter) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(new UserNameSpan(uname, uid));
        sb.append("给 圈主 投了 ");
        sb.append(num);
        sb.append("票");
        return sb;
    }
}
