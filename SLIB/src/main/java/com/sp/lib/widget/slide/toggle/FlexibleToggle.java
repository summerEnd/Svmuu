package com.sp.lib.widget.slide.toggle;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.sp.lib.widget.slide.toggle.shape.LineTransformer;
import com.sp.lib.widget.slide.toggle.shape.SLine;

public abstract class FlexibleToggle extends ToggleView {
    LineTransformer line1;
    LineTransformer line2;
    LineTransformer line3;
    private boolean rotate = false;
    private boolean INIT = false;

    public FlexibleToggle(Context context) {
        this(context, null);
    }

    public FlexibleToggle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexibleToggle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        line1 = new LineTransformer(new SLine(), new SLine());
        line2 = new LineTransformer(new SLine(), new SLine());
        line3 = new LineTransformer(new SLine(), new SLine());
    }

    public void setFinalLine(SLine end1, SLine end2, SLine end3) {
        copy(line1.getShapeEnd(), end1);
        copy(line2.getShapeEnd(), end2);
        copy(line3.getShapeEnd(), end3);
        invalidate();
    }

    void copy(SLine src, SLine data) {
        src.startX = data.startX;
        src.startY = data.startY;
        src.endX = data.endX;
        src.endY = data.endY;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        if (rotate) {
            canvas.rotate(360 * ratio, getWidth() / 2, getHeight() / 2);
        }
        line1.outPut(ratio).draw(canvas, mPaint);
        line2.outPut(ratio).draw(canvas, mPaint);
        line3.outPut(ratio).draw(canvas, mPaint);
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int pTop = getPaddingTop();
        int pBottom = getPaddingBottom();
        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        float finalXRatio = 0.4f;//x轴最终位置与宽度的比例
        float yFloat = 5;//y轴波动
        final float finalX = pLeft + (width - pLeft - pRight) * finalXRatio;
        int midHeight = height / 2;
        line1.getShapeStart().set(pLeft, pTop, width - pRight, pTop);

        line2.getShapeStart().set(pLeft, midHeight, width - pRight, midHeight);

        line3.getShapeStart().set(pLeft, height - pBottom, width - pRight, height - pBottom);
        if (!INIT) {
            INIT = true;
            onCreateFinalGraphic();
        }
    }

    protected abstract void onCreateFinalGraphic();
}
