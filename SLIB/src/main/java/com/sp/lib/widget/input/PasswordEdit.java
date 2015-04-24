package com.sp.lib.widget.input;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.EditText;

import com.sp.lib.R;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class PasswordEdit extends EditText {


    private int textLength;

    private int borderColor;
    private int contentColor;
    private float borderRadius;

    private int passwordLength;
    private float passwordRadius;

    private Paint passwordPaint = new Paint(ANTI_ALIAS_FLAG);
    private Paint borderPaint = new Paint(ANTI_ALIAS_FLAG);

    RectF mRectF = new RectF();
    private int cellWidth;

    public PasswordEdit(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordEdit(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PasswordInputView, 0, 0);
        try {
            borderColor = a.getColor(R.styleable.PasswordInputView_borderColor, Color.GRAY);
            contentColor = a.getColor(R.styleable.PasswordInputView_contentColor, Color.GRAY);
            borderRadius = a.getDimension(R.styleable.PasswordInputView_borderRadius, 5);
            passwordLength = a.getInt(R.styleable.PasswordInputView_passwordLength, 6);
            passwordPaint.setColor(a.getColor(R.styleable.PasswordInputView_passwordColor, Color.BLACK));
            passwordRadius = a.getDimension(R.styleable.PasswordInputView_passwordRadius, 5);
            cellWidth = a.getDimensionPixelSize(R.styleable.PasswordInputView_cellWidth, 50);
        } finally {
            a.recycle();
        }
        borderPaint.setColor(borderColor);
        passwordPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            width = passwordLength * cellWidth;
        }

        if (heightMode != MeasureSpec.EXACTLY) {
            height = width / passwordLength;
        }
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        int width = getWidth();
        int height = getHeight();


        // 外边框
        mRectF.set(0, 0, width, height);
        borderPaint.setColor(borderColor);
        canvas.drawRoundRect(mRectF, borderRadius, borderRadius, borderPaint);

        // 内容区
        mRectF.set(mRectF.left + getPaddingLeft(), mRectF.top + getPaddingTop(),
                mRectF.right - getPaddingRight(), mRectF.bottom - getPaddingBottom());

        borderPaint.setColor(contentColor);
        canvas.drawRoundRect(mRectF, borderRadius, borderRadius, borderPaint);

        // 分割线
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(3);
        for (int i = 1; i < passwordLength; i++) {
            float x = width * i / passwordLength;
            canvas.drawLine(x, 0, x, height, borderPaint);
        }

        // 密码
        float cx, cy = height / 2;
        float half = width / passwordLength / 2;
        for (int i = 0; i < textLength; i++) {
            cx = width * i / passwordLength + half;
            canvas.drawCircle(cx, cy, passwordRadius, passwordPaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.textLength = text.toString().length();
        invalidate();
    }


}
