package com.svmuu.common.entity.notice;

import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;

/**
 * Created by user1 on 2015/7/15.
 * 成为粉丝
 */
public class BeFansBox extends BaseSystemNotice {
    public String date;
    public String uid;
    public String uname;
    public String fans;

    @Override
    protected CharSequence onCreateContent(Html.ImageGetter getter) {
        //xxx 给圈主赠送了 份 <img>

        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append("恭喜：");
        sb.append(new UserNameSpan(uname, uid));
        sb.append("成为 圈主 ");
        sb.append(fans);

        return sb;
    }
}
