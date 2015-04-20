package com.yjy998.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yjy998.R;
import com.yjy998.common.ImageOptions;

public class MenuFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_main_menu, null);
        ImageView avatarImage = (ImageView) inflate.findViewById(R.id.avatarImage);
        ImageLoader.getInstance().displayImage("", avatarImage, ImageOptions.getAvatarInstance(getResources().getDimensionPixelOffset(R.dimen.avatarSize)));
        return inflate;
    }

}
