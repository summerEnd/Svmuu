package com.svmuu.ui.activity.live;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.svmuu.R;

public class LiveModeSelector extends PopupWindow implements RadioGroup.OnCheckedChangeListener{
    public static final int MODE_VIDEO=1;
    public static final int MODE_AUDIO=2;
    public static final int MODE_TEXT=3;

    public LiveModeSelector(Context context) {
        super(context);

        RadioGroup inflate = (RadioGroup) View.inflate(context, R.layout.live_mode_selector, null);
        setContentView(inflate);
        inflate.setOnCheckedChangeListener(this);
        setBackgroundDrawable(new ColorDrawable());
        setWindowLayoutMode(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        setFocusable(true);
        inflate.check(R.id.videoMode);
    }

    public void onModePick(int mode){

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.videoMode:{
                onModePick(MODE_VIDEO);
                break;
            }
            case R.id.audioMode:{
                onModePick(MODE_AUDIO);
                break;
            }
            case R.id.textMode:{
                onModePick(MODE_TEXT);
                break;
            }
        }
    }
}
