package com.yjy998.common.adapter;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yjy998.R;
import com.yjy998.common.entity.Contest;
import com.yjy998.ui.activity.main.my.ContractInfoActivity;

import java.util.LinkedList;
import java.util.List;

public class ContestPagerAdapter extends PagerAdapter implements View.OnClickListener {

    private List<Contest> contests;
    int pageCount;
    LinkedList<View> views = new LinkedList<View>();

    public ContestPagerAdapter(List<Contest> contests) {
        this.contests = contests;
        if (contests == null || contests.isEmpty()) {
            pageCount = 1;
        } else {
            pageCount = (contests.size() - 1) / 3 + 1;
        }
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        if (contests==null||contests.size() == 0) {
            LinearLayout empty = (LinearLayout) View.inflate(container.getContext(), R.layout.empty_layout, null);
            empty.setOrientation(LinearLayout.HORIZONTAL);
            TextView emptyText = (TextView) empty.findViewById(R.id.noDataText);
            emptyText.setText(R.string.no_contest);
            container.addView(empty);

            return empty;
        }

        ViewGroup convertView;
        ViewHolder holder;
        try {
            convertView = (ViewGroup) views.get(position);
            holder = (ViewHolder) convertView.getTag();
        } catch (IndexOutOfBoundsException e) {
            convertView = (ViewGroup) View.inflate(container.getContext(), R.layout.item_game, null);

            holder = new ViewHolder();
            holder.item1 = convertView.findViewById(R.id.item1);
            holder.item2 = convertView.findViewById(R.id.item2);
            holder.item3 = convertView.findViewById(R.id.item3);
            holder.item1.setOnClickListener(this);
            holder.item2.setOnClickListener(this);
            holder.item3.setOnClickListener(this);
            convertView.setTag(holder);
            views.add(convertView);
        }

        int dataPosition = position * 3;
        initItem(holder.item1, dataPosition);
        initItem(holder.item2, dataPosition + 1);
        initItem(holder.item3, dataPosition + 2);

        container.addView(convertView);
        return convertView;
    }

    private void initItem(View item, int dataPosition) {
        try {
            Contest contest = contests.get(dataPosition);
            setVisibility((ViewGroup) item, View.VISIBLE);
            TextView areaText = (TextView) item.findViewById(R.id.areaText);
            TextView rankText = (TextView) item.findViewById(R.id.rankText);
            TextView rateText = (TextView) item.findViewById(R.id.rateText);
            areaText.setText(contest.area);
            if (TextUtils.isEmpty(contest.rank)) {
                ((View) rankText.getParent()).setVisibility(View.GONE);
            } else {
                rankText.setText(contest.rank);
                ((View) rankText.getParent()).setVisibility(View.VISIBLE);
            }
            rateText.setText(item.getContext().getString(R.string.income_rate_f, contest.getProfitRatio() * 100));

            item.setTag(contest);
        } catch (IndexOutOfBoundsException e) {
            setVisibility((ViewGroup) item, View.INVISIBLE);
            item.setTag(null);
        }

    }

    public void setVisibility(ViewGroup item, int visibility) {

        item.setEnabled(visibility == View.VISIBLE);
        for (int i = 0; i < item.getChildCount(); i++) {
            item.getChildAt(i).setVisibility(visibility);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onClick(View v) {
        Contest contest = (Contest) v.getTag();
        if (contest == null) {
            return;
        }
        //这里的id就是比赛的id
        v.getContext().startActivity(new Intent(v.getContext(), ContractInfoActivity.class)
                .putExtra(ContractInfoActivity.EXTRA_CONTRACT_NO, contest.id));
    }

    private class ViewHolder {
        View item1;
        View item2;
        View item3;

    }
}
