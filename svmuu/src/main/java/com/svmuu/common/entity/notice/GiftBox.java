package com.svmuu.common.entity.notice;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.View;

/**
 * Created by user1 on 2015/7/15.
 * 赠送礼物
 */
public class GiftBox extends BaseSystemNotice {
    public String date;
    public String uid;
    public String uname;
    public String gift;
    public String num;
    public String img;

    @Override
    protected CharSequence onCreateContent(Html.ImageGetter getter) {
        //xxx 给圈主赠送了 份 <img>
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(new UserNameSpan(uname, uid));
        sb.append("给 圈主 赠送了");
        sb.append(num).append("粉").append(gift);

        sb.append(createImageSpan(getter, img));
        return sb;
    }
}
