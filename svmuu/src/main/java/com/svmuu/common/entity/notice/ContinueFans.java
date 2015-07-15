package com.svmuu.common.entity.notice;

import android.text.Html;
import android.text.SpannableStringBuilder;

/**
 * Created by user1 on 2015/7/15.
 * 续期粉丝
 */
public class ContinueFans extends BaseSystemNotice {
    public String date;
    public String uid;
    public String uname;
    public String fans;

    @Override
    protected CharSequence onCreateContent(Html.ImageGetter getter) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append("恭喜：");
        sb.append(new UserNameSpan(uname, uid));
        sb.append("续期 圈主 ");
        sb.append(fans);

        return sb;
    }
}
