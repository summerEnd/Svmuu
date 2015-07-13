package com.svmuu.common.adapter.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;

import com.sp.lib.common.util.TextPainUtil;
import com.svmuu.R;

/**
 * Created by user1 on 2015/7/13.
 * 当列表数据为空时展示提示文字
 */
public class EmptyDecoration extends RecyclerView.ItemDecoration {

    private Context context;
    private String empty;

    Paint p = new Paint();
    public EmptyDecoration(Context context,String empty) {
        this.context = context;
        this.empty = empty;
        p.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.text_25px));
        p.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * 获取画笔
     * @return 用来绘制文本的画笔
     */
    public Paint getPaint() {
        return p;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (state.getItemCount() == 0) {
            c.drawText(empty, parent.getWidth() / 2, parent.getHeight() / 2 - TextPainUtil.getBaseLineOffset(p), p);
        }
        super.onDrawOver(c, parent, state);
    }
}
