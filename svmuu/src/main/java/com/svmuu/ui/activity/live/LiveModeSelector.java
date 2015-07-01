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

    public void onClicked(int position){

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.videoMode:{
                onClicked(0);
                break;
            }
            case R.id.audioMode:{
                onClicked(1);
                break;
            }
            case R.id.textMode:{
                onClicked(2);
                break;
            }
        }
    }
}
