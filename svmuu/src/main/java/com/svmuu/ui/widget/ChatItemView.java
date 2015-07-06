package com.svmuu.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.svmuu.R;

public class ChatItemView extends LinearLayout {

    boolean isSelf;
    MsgLayoutInfo info;
    private ImageView leftAvatarView;
    private TextView leftJob;
    private TextView text1;
    private TextView text2;
    private ImageView rightAvatarView;
    private TextView rightJob;
    private LinearLayout contentView;
    private LinearLayout msgWrapLayout;
    private LinearLayout msgLayout;

    public ChatItemView(Context context, boolean isSelf) {
        super(context);
        init(context, isSelf);
    }

    public ChatItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        String text;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChatItemView);
        isSelf = a.getBoolean(R.styleable.ChatItemView_isSelf, false);
        text = a.getString(R.styleable.ChatItemView_msgContent);
        a.recycle();
        init(context, isSelf);
        //todo forTest
        info.nameText.setText("Lincoln");
        info.timeText.setText("21:00");
        setText(text);

    }

    void init(Context context, boolean isSelf) {

        contentView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.chat_msg_layout, this, true);
        leftAvatarView = (ImageView) findViewById(R.id.leftAvatarView);
        leftJob = (TextView) findViewById(R.id.leftJob);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        rightAvatarView = (ImageView) findViewById(R.id.rightAvatarView);
        rightJob = (TextView) findViewById(R.id.rightJob);
        msgWrapLayout = (LinearLayout) findViewById(R.id.msgWrapLayout);
        msgLayout = (LinearLayout) findViewById(R.id.msgLayout);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.TOP);

        info = new MsgLayoutInfo();
        info.fansIcon = (ImageView) findViewById(R.id.fansIcon);
        info.contentText = (TextView) findViewById(R.id.msgContent);

        setMsgType(isSelf);


    }

    public void setMsgType(boolean isSelf) {
        this.isSelf = isSelf;
        if (isSelf) {
            msgLayout.setGravity(Gravity.RIGHT);
            msgWrapLayout.setGravity(Gravity.RIGHT);
            info.avatarView = rightAvatarView;
            info.nameText = text2;
            info.timeText = text1;
            info.jobText=rightJob;
            ((View) leftAvatarView.getParent()).setVisibility(INVISIBLE);
            ((View) rightAvatarView.getParent()).setVisibility(VISIBLE);
        } else {
            msgWrapLayout.setGravity(Gravity.LEFT);
            msgLayout.setGravity(Gravity.LEFT);
            info.avatarView = leftAvatarView;
            info.nameText = text1;
            info.timeText = text2;
            info.jobText=leftJob;
            ((View) rightAvatarView.getParent()).setVisibility(INVISIBLE);
            ((View) leftAvatarView.getParent()).setVisibility(VISIBLE);
        }
    }

    public MsgLayoutInfo getInfo() {
        return info;
    }

    public void setText(String text) {
        info.contentText.setText(text);
    }

    class ImageGet implements Html.ImageGetter {
        @Override
        public Drawable getDrawable(String source) {
            return null;
        }
    }

    public class MsgLayoutInfo {
        public ImageView avatarView;
        public ImageView fansIcon;
        public TextView nameText;
        public TextView timeText;
        public TextView contentText;
        public TextView jobText;
    }
}
