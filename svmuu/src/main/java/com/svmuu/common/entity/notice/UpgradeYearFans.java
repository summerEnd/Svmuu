package com.svmuu.common.entity.notice;

import android.text.Html;
import android.text.SpannableStringBuilder;

/**
 * Created by user1 on 2015/7/15.
 * 升级为年粉
 */
public class UpgradeYearFans extends BaseSystemNotice {
    public String date;
    public String uid;
    public String uname;
    public String fans;

    @Override
    protected CharSequence onCreateContent(Html.ImageGetter getter) {

        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append("恭喜：");
        sb.append(new UserNameSpan(uname, uid));
        sb.append("升级为 圈主 ");
        sb.append(fans);

        return sb;
    }
}
