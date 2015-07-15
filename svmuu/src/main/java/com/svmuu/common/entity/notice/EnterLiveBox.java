package com.svmuu.common.entity.notice;

import android.text.Html;
import android.text.SpannableStringBuilder;

/**
 * Created by user1 on 2015/7/15.
 * 进入直播间
 */
public class EnterLiveBox extends BaseSystemNotice {
    public String date;//=>日期：('月-日 时:分:秒'),
    public String uid;//=> 用户ID,
    public String uname;//=> 用户姓名,
    public String title;//=> 用户级别,

    @Override
    protected CharSequence onCreateContent(Html.ImageGetter getter) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append("欢迎");
        sb.append(new UserNameSpan(uname, uid));
        sb.append(" ");
        sb.append(title);
        sb.append(" ");
        sb.append("来到我们直播间");

        return sb;
    }
}
