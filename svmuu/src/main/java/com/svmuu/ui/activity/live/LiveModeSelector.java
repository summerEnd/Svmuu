package com.svmuu.ui.activity.live;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.svmuu.R;
import com.svmuu.common.LiveManager;

public class LiveModeSelector extends PopupWindow implements RadioGroup.OnCheckedChangeListener{


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
                onModePick(LiveManager.MODE_VIDEO);
                break;
            }
            case R.id.audioMode:{
                onModePick(LiveManager.MODE_AUDIO);
                break;
            }
            case R.id.textMode:{
                onModePick(LiveManager.MODE_TEXT);
                break;
            }
        }
    }
}
