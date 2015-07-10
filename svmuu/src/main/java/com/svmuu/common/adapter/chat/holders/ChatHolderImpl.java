package com.svmuu.common.adapter.chat.holders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ParagraphStyle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.svmuu.R;
import com.svmuu.common.adapter.BaseHolder;
import com.svmuu.common.adapter.chat.ChatParams;
import com.svmuu.common.entity.Chat;


public class ChatHolderImpl extends BaseHolder<Chat> {

    private ChatParams params;

    //圈主
    public static final int TYPE_MASTER = 0;
    public static final int TYPE_NORMAL = 1;//普通
    public static final int TYPE_WHISPER = 2; //悄悄话
    public static final int TYPE_JP = 3;//解盘
    public static final int TYPE_QA = 4;//问答
    public static final int TYPE_NOTICE = 5;//公告

    public final ImageView chatItemAvatar;
    public final TextView chatItemTime;
    public final TextView chatItemNick;
    public final TextView chatItemContent;
    public final ImageView chatItemFans;
    public final TextView chatItemJob;

    public static ChatHolderImpl newInstance(Context context, ViewGroup parent, int type) {
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (type) {
            case TYPE_MASTER: {
                return new MasterChatHolder(inflater.inflate(R.layout.chat_item_master, parent, false));
            }
            case TYPE_NORMAL: {
                return new ChatNormalHolder(inflater.inflate(R.layout.chat_item_normal, parent, false));
            }
            case TYPE_WHISPER: {
                return new WhisperChatHolder(inflater.inflate(R.layout.chat_item_whisper, parent, false));
            }
//            case TYPE_JP: {
//                return new WhisperChatHolder(inflater.inflate(R.layout.chat_item_jp, parent, false));
//            }
//            case TYPE_QA: {
//                return new WhisperChatHolder(inflater.inflate(R.layout.chat_item_qa, parent, false));
//            }
            case TYPE_NOTICE: {
                return new ChatNoticeHolder(inflater.inflate(R.layout.chat_item_notice, parent, false));
            }
        }
        return new ChatNormalHolder(inflater.inflate(R.layout.chat_item_normal, parent, false));
    }

    protected ChatHolderImpl(View itemView) {
        super(itemView);
        chatItemAvatar = (ImageView) findViewById(R.id.chatItemAvatar);
        chatItemTime = (TextView) findViewById(R.id.chatItemTime);
        chatItemNick = (TextView) findViewById(R.id.chatItemNick);
        chatItemContent = (TextView) findViewById(R.id.chatItemContent);
        chatItemFans = (ImageView) findViewById(R.id.chatItemFans);
        chatItemJob = (TextView) findViewById(R.id.chatItemJob);
        if (chatItemContent != null) {
            //chatItemContent.setMovementMethod(ChatMovementMethod.getInstance());
        }
    }

    public ChatParams getParams() {
        return params;
    }

    /**
     * 在adapter启动之前就要设置这个参数，并且不能为空
     *
     * @param params 聊天的基本信息
     */
    public void setParams(@NonNull ChatParams params) {
        this.params = params;
    }

    /**
     * 展示聊天的基本信息
     *
     * @param chat    聊天信息
     * @param options 用来加载头像
     */
    public void displayWith(Chat chat, DisplayImageOptions options) {
        Context context = itemView.getContext();
        if (chatItemFans != null) {
            //1、普通粉丝；2 铁粉；3 年粉 4 季粉',
            switch (chat.fans_type) {
                case "4":
                case "2": {
                    chatItemFans.setImageResource(R.drawable.iron_fans);
                    break;
                }
                case "3": {
                    chatItemFans.setImageResource(R.drawable.year_fans);
                    break;
                }

                default: {
                    chatItemFans.setVisibility(View.GONE);
                }
            }
        }

        if (chatItemTime != null) {
            chatItemTime.setText(chat.time_m);
        }

        if (chatItemJob != null) {
            if ("1".equals(chat.is_admin)) {
                chatItemJob.setVisibility(View.VISIBLE);
                chatItemJob.setText(context.getString(R.string.manager));
            } else {
                chatItemJob.setVisibility(View.INVISIBLE);
            }
        }

        if (chatItemAvatar != null) {
            ImageLoader.getInstance().displayImage(chat.uface, chatItemAvatar, options);
        }

        if (chatItemNick != null) {
            chatItemNick.setText(chat.uname);
        }
    }

    /**
     * 展示聊天消息
     */
    public void displayContent(Chat chat, Html.ImageGetter imageGetter) {
        Spanned text = Html.fromHtml(chat.content, imageGetter, null);

        chatItemContent.setText(text);
    }

    private ClickableSpan mSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {

        }
    };

}
