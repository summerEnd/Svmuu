package com.svmuu.common.entity.notice;

import android.text.Html;
import android.text.SpannableStringBuilder;

/**
 * Created by user1 on 2015/7/15.
 * 更新文字宝盒
 */
public class UpdateTextBox extends BaseSystemNotice {
    public String date;
    public String uid;
    public String uname;
    public String bid;
    public String bname;

    @Override
    protected CharSequence onCreateContent(Html.ImageGetter getter) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(new UserNameSpan(uname, uid));
        sb.append("更新了文字宝盒");
        sb.append(bname);

        return sb;
    }
}
