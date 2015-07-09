package com.svmuu.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sp.lib.common.image.drawable.CircleLRDrawable;
import com.sp.lib.common.util.ViewUtil;
import com.svmuu.R;

/**
 * 通用搜索
 */
public class CustomSearchView extends LinearLayout {

    CircleLRDrawable drawable;

    public static final int STYLE_CLICK = 0;
    public static final int STYLE_EDIT = 1;

    private Callback mCallback;
    ProgressBar progressBar;
    private EditText editSearch;
    private TextView textSearch;
    private ImageView closeIcon;
    private ImageView searchIcon;
    private OnClickListener jumpListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCallback != null) {
                mCallback.onJump();
            }
        }
    };
    private OnClickListener searchListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //没有输入内容就不发起搜索,产品这么说的- -!
            String content = editSearch.getText().toString();
            if (TextUtils.isEmpty(content)) {
                return;
            }
            //显示进度条
            progressBar.setVisibility(VISIBLE);
            if (mCallback != null) {
                mCallback.onSearch(content);
            }
        }
    };

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
        View v = View.inflate(context, R.layout.search_view, this);
        searchIcon = (ImageView) v.findViewById(R.id.search);
        closeIcon = (ImageView) v.findViewById(R.id.cross);
        editSearch = (EditText) findViewById(R.id.editSearch);
        textSearch = (TextView) findViewById(R.id.textSearch);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        closeIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editSearch.setText(null);
            }
        });
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.length() == 0) {
                    closeIcon.setVisibility(INVISIBLE);
                } else {
                    closeIcon.setVisibility(VISIBLE);
                }
            }
        });
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (mCallback!=null){
                        mCallback.onSearch(editSearch.getText().toString());
                    }
                }
                return false;
            }
        });
        setStyle(style);

        //背景图片
        drawable = new CircleLRDrawable(Color.WHITE);
        drawable.setBorderColor(Color.LTGRAY);
        drawable.setDrawBord(true);
        ViewUtil.setBackground(this, drawable);
    }

    public void setStyle(int style) {
        if (style == STYLE_EDIT) {
            setOnClickListener(null);
            closeIcon.setVisibility(VISIBLE);
            editSearch.setVisibility(VISIBLE);
            textSearch.setVisibility(INVISIBLE);
            searchIcon.setOnClickListener(searchListener);
        } else {
            setOnClickListener(jumpListener);
            closeIcon.setVisibility(INVISIBLE);
            editSearch.setVisibility(INVISIBLE);
            textSearch.setVisibility(VISIBLE);
            searchIcon.setOnClickListener(null);
        }
    }

    public void onSearchComplete() {
        progressBar.setVisibility(INVISIBLE);
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }
}
