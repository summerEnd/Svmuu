package com.yjy998.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sp.lib.common.support.adapter.ViewHolderAdapter;
import com.yjy998.R;
import com.yjy998.entity.Contract;
import com.yjy998.entity.Game;

import java.util.List;

public class GameAdapter extends ViewHolderAdapter<Game, Object> {

    final int color = 0xFFDDDDDD;

    public GameAdapter(Context context, List<Game> data) {
        super(context, data, R.layout.item_game);
    }

    @Override
    public Object doFindIds(View convertView) {
        ViewHolder holder = new ViewHolder();
        return holder;
    }

    @Override
    public void displayView(View convertView, Object holder, int position) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 1;
        convertView.setLayoutParams(params);
    }

    private class ViewHolder {

    }
}
