package com.yjy998.activity;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yjy998.R;

public class MenuFragment extends BaseFragment {
    boolean checked = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_main_menu, null);
        ImageView avatarImage = (ImageView) inflate.findViewById(R.id.avatarImage);

        return inflate;
    }

}
