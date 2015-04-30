package com.yjy998.ui.view.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yjy998.R;

import java.util.ArrayList;
import java.util.Random;

public class GView extends View {

    private String[] leftLabels = new String[]{"11.29", "11.02", "10.03", "10.02", "10.00"};
    private String[] rightLabels = new String[]{"1.56%", "1.26%", "0.00%", "-1.26%", "-1.56%"};
    private String[] bottomLabels = new String[]{"", "11:30", "00.30", "14.00", ""};
    TextPaint labelPaint = new TextPaint();
    Paint framePaint = new Paint();
    Paint linePaint = new Paint();
    RectF frame = new RectF();

    float textVerticalOffset;
    float xMin;
    float xMax;
    float yMin;
    float yMax;
    ArrayList<GLine> lines = new ArrayList<GLine>();

    float touchX;
    float touchY;

    public GView(Context context) {
        this(context, null);
    }

    public GView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.GView);
        xMin = a.getFloat(R.styleable.GView_xMin, 0);
        xMax = a.getFloat(R.styleable.GView_xMax, 240);
        yMin = a.getFloat(R.styleable.GView_yMin, 0);
        yMax = a.getFloat(R.styleable.GView_yMax, 50);
        a.recycle();

        labelPaint.setTextSize(20);
        labelPaint.setTextAlign(Paint.Align.CENTER);
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setColor(Color.GRAY);
        linePaint.setColor(Color.WHITE);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2);
        //计算数字高度
        Rect rect = new Rect();
        labelPaint.getTextBounds("1", 0, 1, rect);
        textVerticalOffset = rect.height() / 2;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureFrame();
    }


    /**
     * 添加一个曲线
     */
    public void addLine(GLine line) {
        lines.add(line);
    }

    private void initGraphic() {

        if (frame.width() == 0) {
            return;
        }

        for (GLine line : lines) {
            createGraphic(line);
        }

        for (int i = 0; i < leftLabels.length; i++) {
            leftLabels[i] = String.format("%.2f", yMin + (yMax - yMin) / 4 * i);
        }
        float base = (yMax + yMin) / 2;
        for (int i = 0; i < rightLabels.length; i++) {
            float value = Float.valueOf(leftLabels[i]);
            rightLabels[i] = String.format("%.2f%%", (value - base) / base);
        }

    }

    private void createGraphic(GLine line) {

        if (line.isCreated()) {
            return;
        }
        float[] values = line.getValues();

        //单位1的长度
        float cellWidth = frame.width() / (xMax - xMin);
        float cellHeight = frame.height() / (yMax - yMin);

        float firstPointX = frame.left;
        float firstPointY = frame.top + (yMax - values[0]) * cellHeight;

        line.moveTo(firstPointX, firstPointY);
        for (int i = 0; i < values.length; i++) {
            line.lineTo(frame.left + i * cellWidth, frame.top + (yMax - values[i]) * cellHeight);
        }
        int lineColor = line.getLineColor();
        if (lineColor != 0) line.getPaint().setColor(lineColor);
        if (line.isDrawBelowColor()) {
            line.lineTo(frame.left + (values.length - 1) * cellWidth, frame.bottom);
            line.lineTo(frame.left, frame.bottom);
            line.lineTo(firstPointX, firstPointY);
            line.close();

            LinearGradient shader = new LinearGradient(
                    frame.left, 0,
                    frame.left, frame.height(),
                    new int[]{line.getStartColor(), line.getEndColor()},
                    new float[]{0.1f, 1},
                    Shader.TileMode.MIRROR);
            line.getPaint().setShader(shader);
        } else {
            line.getPaint().setStyle(Paint.Style.STROKE);
        }
        line.setCreated(true);

    }

    private void measureFrame() {
        float leftLabelMax = 0;
        float rightLabelMax = 0;
        for (String leftLabel : leftLabels) {
            leftLabelMax = Math.max(labelPaint.measureText(leftLabel), leftLabelMax);
        }
        for (String rightLabel : rightLabels) {
            rightLabelMax = Math.max(labelPaint.measureText(rightLabel), rightLabelMax);
        }


        frame.set(leftLabelMax + getPaddingLeft(), getPaddingTop(), getMeasuredWidth() - rightLabelMax - getPaddingRight(), getMeasuredHeight() - textVerticalOffset * 2 - getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initGraphic();
        drawLeftLabels(canvas);
        drawFrame(canvas);
        drawRightLabels(canvas);
        drawBottomLabels(canvas);
        drawChart(canvas);
        drawTouchLine(canvas);
    }

    private void drawTouchLine(Canvas canvas) {
        if (!frame.contains(touchX, touchY)) {
            return;
        }

        float cellHeight = frame.height() / (yMax - yMin);
        float cellWidth = frame.width() / (xMax - xMin);
        int position = (int) ((touchX - frame.left) / frame.width() * (xMax - xMin));
        float[] values = lines.get(0).getValues();
        position = Math.min(position, values.length - 1);
        float x = frame.left + position * cellWidth;
        float y = frame.top + (yMax - values[position]) * cellHeight;

        canvas.drawLine(x, frame.bottom, x, frame.top, linePaint);

        canvas.drawLine(frame.left, y, frame.right, y, linePaint);

    }

    private void drawBottomLabels(Canvas canvas) {
        labelPaint.setTextAlign(Paint.Align.CENTER);
        labelPaint.setColor(Color.BLACK);

        float width = frame.width() / (bottomLabels.length - 1);
        float topOffset = frame.bottom + textVerticalOffset * 2 + 2;
        for (int i = 0; i < bottomLabels.length; i++) {
            canvas.drawText(bottomLabels[i], frame.left + width * i, topOffset, labelPaint);
        }
    }

    private void drawLeftLabels(Canvas canvas) {
        labelPaint.setTextAlign(Paint.Align.RIGHT);
        float height = frame.height() / (leftLabels.length - 1);
        for (int i = 0; i < leftLabels.length; i++) {
            float y = frame.bottom + textVerticalOffset - height * i;
            labelPaint.setColor(getLeftLabelColor(i));
            canvas.drawText(leftLabels[i], frame.left - 2, y, labelPaint);
        }
    }

    /**
     * 获取左边标签的颜色
     *
     * @param position 从0开始
     */
    private int getLeftLabelColor(int position) {
        switch (position) {
            case 0:
            case 1:
                return Color.GREEN;

            case 3:
            case 4:
                return Color.RED;
        }
        return Color.BLACK;
    }

    /**
     * 获取左边标签的颜色
     *
     * @param position 从0开始
     */
    private int getRightLabelColor(int position) {
        return getLeftLabelColor(position);
    }

    private void drawRightLabels(Canvas canvas) {
        labelPaint.setTextAlign(Paint.Align.LEFT);
        float height = frame.height() / (rightLabels.length - 1);
        float leftOffset = frame.right;
        for (int i = 0; i < rightLabels.length; i++) {
            labelPaint.setColor(getRightLabelColor(i));
            float y = frame.bottom + textVerticalOffset - height * i;
            canvas.drawText(rightLabels[i], leftOffset + 2, y, labelPaint);
        }
    }

    private void drawFrame(Canvas canvas) {

        canvas.drawRect(frame, framePaint);

        float topOffset = frame.top;
        float height = frame.height() / (leftLabels.length - 1);
        for (int i = 0; i < leftLabels.length; i++) {
            float y = topOffset + height * i;
            canvas.drawLine(frame.left, y, frame.right, y, framePaint);
        }

        float width = frame.width() / (bottomLabels.length - 1);
        for (int i = 0; i < bottomLabels.length; i++) {
            float x = frame.left + width * i;
            canvas.drawLine(x, frame.bottom, x, frame.top, framePaint);
        }
    }

    private void drawChart(Canvas canvas) {
        for (GLine line : lines) {
            Paint paint = line.getPaint();
            if (line.isDrawBelowColor() && line.getLineColor() != 0) {
                canvas.save();
                Shader shader = paint.getShader();

                canvas.translate(-1, -2);
                paint.setShader(null);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawPath(line, paint);
                paint.setShader(shader);
                paint.setStyle(Paint.Style.FILL);
                canvas.restore();
            }
            canvas.drawPath(line, paint);
        }
    }

    /**
     * 设置x轴取值范围
     *
     * @param xMin 最小值
     * @param xMax 最大值
     */
    public void setRangeX(float xMin, float xMax) {
        this.xMin = xMin;
        this.xMax = xMax;

    }

    /**
     * 设置Y轴取值范围
     *
     * @param minY 最小值
     * @param maxY 最大值
     */
    public void setRangeY(float minY, float maxY) {
        this.yMax = maxY;
        this.yMin = minY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                touchX = event.getX();
                touchY = event.getY();
                invalidate();
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                touchX = -1;
                touchY = -1;
                break;
            }
        }

        return true;
    }
}
