package com.svmuu.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sp.lib.common.image.drawable.CircleLRDrawable;
import com.svmuu.R;

/**
 * 通用搜索
 */
public class CustomSearchView extends LinearLayout {

    CircleLRDrawable drawable;
    public CustomSearchView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs, 0);
    }

    public CustomSearchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    @SuppressWarnings("deprecation")
    private void init(Context context,AttributeSet attrs, int defStyle) {

        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.CustomSearchView);
        int iconSize=(int)a.getDimension(R.styleable.CustomSearchView_iconSize,LayoutParams.WRAP_CONTENT);
        a.recycle();
        ImageView searchIcon = new ImageView(context);
        searchIcon.setImageResource(R.drawable.ic_search);

        EditText searchContent = new EditText(context);
        searchContent.setHint(R.string.search_hint);

        ImageView closeIcon = new ImageView(context);
        closeIcon.setImageResource(R.drawable.ic_clear_search);

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams iconParams = new LayoutParams(iconSize, iconSize);
        LayoutParams editParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        editParams.weight = 1;
        addView(searchIcon, iconParams);
        addView(searchContent, editParams);
        addView(closeIcon, iconParams);
        drawable=new CircleLRDrawable(Color.WHITE);
        drawable.setBorderColor(Color.LTGRAY);
        drawable.setDrawBord(true);
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN){
            setBackground(drawable);
        }else{
            setBackgroundDrawable(drawable);

        }
    }


}
