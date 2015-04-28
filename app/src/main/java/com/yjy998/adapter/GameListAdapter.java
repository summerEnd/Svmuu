package com.yjy998.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.sp.lib.common.support.adapter.ViewHolderAdapter;
import com.yjy998.R;
import com.yjy998.entity.Game;
import com.yjy998.ui.activity.game.RankActivity;
import com.yjy998.ui.view.TwoTextItem;

import java.util.List;

public class GameListAdapter extends ViewHolderAdapter<Game, Object> implements View.OnClickListener{


    public GameListAdapter(Context context, List<Game> data) {
        super(context, data, R.layout.list_item_game);
    }

    @Override
    public Object doFindIds(View convertView) {

        ViewHolder holder = new ViewHolder();
        holder.titleText = (TextView) convertView.findViewById(R.id.titleText);
        holder.rankText = (TextView) convertView.findViewById(R.id.rankText);
        holder.membersText = (TwoTextItem) convertView.findViewById(R.id.membersText);
        holder.typeText = (TwoTextItem) convertView.findViewById(R.id.typeText);
        holder.gameTime = (TextView) convertView.findViewById(R.id.gameTime);
        holder.rankText.setOnClickListener(this);
        return holder;
    }

    @Override
    public void displayView(View convertView, Object holder, int position) {

    }

    @Override
    public void onClick(View v) {
        v.getContext().startActivity(new Intent(v.getContext(), RankActivity.class));
    }

    private class ViewHolder {
        TextView titleText;
        TextView rankText;
        TwoTextItem membersText;
        TwoTextItem typeText;
        TextView gameTime;
    }
}
