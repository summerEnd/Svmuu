package com.yjy998.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.sp.lib.common.support.adapter.ViewHolderAdapter;
import com.yjy998.R;
import com.yjy998.entity.Contest;
import com.yjy998.ui.activity.contest.RankActivity;
import com.yjy998.ui.view.TwoTextItem;

import java.util.List;

public class ContestListAdapter extends ViewHolderAdapter<Contest, Object> implements View.OnClickListener {


    public ContestListAdapter(Context context, List<Contest> data) {
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
        ViewHolder viewHolder = (ViewHolder) holder;
        Contest contest = getItem(position);
        if (contest == null) {
            return;
        }
        viewHolder.gameTime.setText(convertView.getContext().getString(R.string.game_start_end_s, contest.startTime + " - " + contest.endTime));
        viewHolder.titleText.setText(contest.name);
        viewHolder.membersText.setText(contest.attenders);
        viewHolder.typeText.setText(contest.getType());
        viewHolder.rankText.setTag(contest.id);
    }

    @Override
    public void onClick(View v) {
        v.getContext().startActivity(new Intent(v.getContext(), RankActivity.class)
                .putExtra(RankActivity.EXTRA_ID, (String) v.getTag()));
    }

    private class ViewHolder {
        TextView titleText;
        TextView rankText;
        TwoTextItem membersText;
        TwoTextItem typeText;
        TextView gameTime;
    }
}
