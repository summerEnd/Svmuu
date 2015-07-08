package com.svmuu.ui.activity.live;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckedTextView;
import android.widget.PopupWindow;

import com.svmuu.R;

public class LiveModeSelector extends PopupWindow implements View.OnClickListener {

    CheckedTextView videoMode;
    CheckedTextView audioMode;
    CheckedTextView textMode;
    private final ViewGroup contentView;

    public LiveModeSelector(Context context) {
        super(context);

        contentView = (ViewGroup) View.inflate(context, R.layout.live_mode_selector, null);
        setContentView(contentView);
        (videoMode = (CheckedTextView) contentView.findViewById(R.id.videoMode)).setOnClickListener(this);
        (audioMode = (CheckedTextView) contentView.findViewById(R.id.audioMode)).setOnClickListener(this);
        (textMode = (CheckedTextView) contentView.findViewById(R.id.textMode)).setOnClickListener(this);
        setBackgroundDrawable(new ColorDrawable());
        setWindowLayoutMode(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        setFocusable(true);
    }

    public void check(int position) {
        int count = contentView.getChildCount();
        for (int i = 0; i < count; i++) {
            CheckedTextView child = (CheckedTextView) contentView.getChildAt(i);
            child.setChecked(position == i);
        }
    }

    public void onClicked(int position) {
    }

    @Override
    public void onClick(View v) {
        int position = 0;
        switch (v.getId()) {
            case R.id.videoMode: {
                position = 0;
                break;
            }
            case R.id.audioMode: {
                position = 1;
                break;
            }
            case R.id.textMode: {
                position = 2;
                break;
            }
        }
        onClicked(position);
        check(position);
    }
}
