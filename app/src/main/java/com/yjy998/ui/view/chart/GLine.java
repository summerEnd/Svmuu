package com.yjy998.ui.view.chart;

import android.graphics.Paint;
import android.graphics.Path;

public class GLine extends Path {
    private Paint mPaint = new Paint();

    private float values[];
    private boolean drawBelowColor;
    private int startColor;
    private int endColor;
    private int lineColor;
    private boolean isCreated = false;

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        this.isCreated = created;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getStartColor() {
        return startColor;
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
    }

    public int getEndColor() {
        return endColor;
    }

    public void setEndColor(int endColor) {
        this.endColor = endColor;
    }

    public boolean isDrawBelowColor() {
        return drawBelowColor;
    }

    public void setDrawBelowColor(boolean drawBelowColor) {
        this.drawBelowColor = drawBelowColor;
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }

    public Paint getPaint() {
        return mPaint;
    }

}
