package com.svmuu.common.adapter.chat.holders;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sp.lib.common.util.ContextUtil;
import com.svmuu.R;
import com.svmuu.common.entity.Chat;
import com.svmuu.common.entity.Question;

/**
 * Created by user1 on 2015/7/9.
 * 问答
 */
public class ChatQAHolder extends ChatHolderImpl {
    public ImageView questionAvatar;
    public TextView questionName;
    public TextView questionTime;
    public TextView questionContent;
    public TextView questionJob;

    protected ChatQAHolder(View itemView) {
        super(itemView);
        questionAvatar = (ImageView) findViewById(R.id.questionAvatar);
        questionContent = (TextView) findViewById(R.id.questionContent);
        questionName = (TextView) findViewById(R.id.questionName);
        questionTime = (TextView) findViewById(R.id.questionTime);
        questionJob = (TextView) findViewById(R.id.questionJob);
    }

    @Override
    public void displayWith(Chat chat, DisplayImageOptions options) {
        super.displayWith(chat, options);
        Question question = chat.question;
        ImageLoader.getInstance().displayImage(question.uface, questionAvatar, options);
        questionName.setText(question.uname);
        questionTime.setText(question.time_m);
        if (questionJob != null) {
            if ("1".equals(question.is_admin)) {
                questionJob.setVisibility(View.VISIBLE);
                questionJob.setText(R.string.manager);
            } else {
                questionJob.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void displayContent(Chat chat, Html.ImageGetter imageGetter,Html.TagHandler handler) {
        super.displayContent(chat, imageGetter,handler);
        Question question = chat.question;
        questionContent.setText(Html.fromHtml(question.content, imageGetter, handler));
    }

    public static class QADecoration extends RecyclerView.ItemDecoration {

        Drawable qa;

        public QADecoration(Context context) {
            qa = context.getResources().getDrawable(R.drawable.qa_bg);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = parent.getChildAt(i);
                RecyclerView.ViewHolder childViewHolder = parent.getChildViewHolder(child);

                if (childViewHolder instanceof ChatQAHolder) {
                    ChatQAHolder holder = (ChatQAHolder) childViewHolder;
                    int left = holder.questionContent.getLeft();
                    int top = child.getTop();
                    int bottom = child.getBottom();
                    int right = child.getRight();
                    qa.setBounds(left-3,top+3,right+3,bottom+3);
                    qa.draw(c);
                }
            }
        }

    }
}
