package com.svmuu.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sp.lib.common.image.drawable.CircleLRDrawable;
import com.sp.lib.common.util.ViewUtil;
import com.svmuu.R;

/**
 * 通用搜索
 */
public class CustomSearchView extends LinearLayout {

    CircleLRDrawable drawable;

    private static final int STYLE_CLICK = 0;
    private static final int STYLE_EDIT = 1;

    private Callback mCallback;

    public interface Callback {
        //在编辑状态下，点击搜索时触发
        void onSearch(String key);

        //在不可编辑状态下点击时触发
        void onJump();
    }

    public CustomSearchView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CustomSearchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    @SuppressWarnings("deprecation")
    private void init(Context context, AttributeSet attrs, int defStyle) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomSearchView);
        int iconSize = (int) a.getDimension(R.styleable.CustomSearchView_iconSize, LayoutParams.WRAP_CONTENT);
        int style = a.getInt(R.styleable.CustomSearchView_searchStyle, STYLE_EDIT);
        a.recycle();

        ImageView searchIcon = new ImageView(context);
        searchIcon.setImageResource(R.drawable.ic_search);

        final TextView searchContent;

        ImageView closeIcon = new ImageView(context);
        closeIcon.setImageResource(R.drawable.ic_clear_search);
        if (style == STYLE_EDIT) {
            searchContent = new EditText(context);
            closeIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchContent.setText(null);
                }
            });

            searchIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onSearch(searchContent.getText().toString());
                    }
                }
            });
        } else {
            searchContent = new TextView(context);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onJump();
                    }
                }
            });
        }

        searchContent.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
        searchContent.setHint(R.string.search_hint);
        ViewUtil.setBackground(searchContent,new ColorDrawable());

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams iconParams = new LayoutParams(iconSize, iconSize);
        LayoutParams editParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        editParams.weight = 1;
        addView(searchIcon, iconParams);
        addView(searchContent, editParams);
        addView(closeIcon, iconParams);
        //背景图片
        drawable = new CircleLRDrawable(Color.WHITE);
        drawable.setBorderColor(Color.LTGRAY);
        drawable.setDrawBord(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);

        }
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }
}
