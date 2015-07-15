package com.svmuu.common.entity.notice;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.svmuu.ui.activity.live.LiveActivity;

/**
 * Created by user1 on 2015/7/15.
 * 系统公告
 */
public class BaseSystemNotice {
    public String type;//系统公告类型
    private CharSequence content;

    public static BaseSystemNotice newInstance(String type) {
        switch (type) {
            case "creatwbox": {
                return new CreateTextBox();
            }
            case "editbox": {
                return new UpdateTextBox();
            }
            case "creatvbox": {
                return new CreateVideoBox();
            }
            case "gift": {
                return new GiftBox();
            }
            case "vote": {
                return new VoteBox();
            }
            case "fans": {
                return new BeFansBox();
            }
            case "buybox": {
                return new BuyBox();
            }
            case "renewal": {
                return new ContinueFans();
            }
            case "upgrade": {
                return new UpgradeYearFans();
            }
            case "enter": {
                return new EnterLiveBox();
            }
            default:
                throw new RuntimeException("type " + type + " not supported!");
        }
    }

    /**
     * 在这里拼接公告的内容
     *
     * @return 返回公告的内容
     */
    public final CharSequence getContent(Html.ImageGetter imageGetter) {
        if (content == null) {
            content = onCreateContent(imageGetter);
        }
        return content;
    }

    protected CharSequence onCreateContent(Html.ImageGetter getter) {
        return "please implements this method";
    }

    /**
     * 创建图片span
     */
    protected CharSequence createImageSpan(Html.ImageGetter getter, String imageUrl) {
        return Html.fromHtml("<img src=\"" + imageUrl + "\"/>", getter, null);
    }

    protected CharSequence createClickSpan(String source, ClickableSpan span) {
        SpannableString spannableString = new SpannableString(source);
        spannableString.setSpan(span, 0, source.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static class PinkClickSpan extends ClickableSpan {
        @Override
        public void onClick(View widget) {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(0xffD82A4B);
            ds.setUnderlineText(true);
        }
    }

    public static class UserNameSpan extends SpannableString {
        String name;
        String uid;

        public UserNameSpan(String name, String uid) {
            super(name);
            this.name = name;
            this.uid = uid;
            setSpan(new PinkClickSpan() {
                @Override
                public void onClick(View widget) {
                    UserNameSpan.this.onClick(widget);
                }
            }, 0, name.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        public void onClick(View widget) {
//            Context context = widget.getContext();
//            context.startActivity(new Intent(context, LiveActivity.class)
//                    .putExtra(LiveActivity.EXTRA_QUANZHU_ID, uid));
        }

    }
}
