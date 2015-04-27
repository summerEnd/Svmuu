package com.yjy998.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;
import com.yjy998.entity.Game;

import java.util.LinkedList;
import java.util.List;

public class GamePagerAdapter extends PagerAdapter {

    private List<Game> games;
    int pageCount;
    LinkedList<View> views = new LinkedList<View>();

    public GamePagerAdapter(List<Game> games) {
        this.games = games;
        if (games.isEmpty()) {
            pageCount = 0;
        } else {
            pageCount = (games.size() - 1) / 3 + 1;
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
        ViewGroup convertView;
        ViewHolder holder;
        try {
            convertView = (ViewGroup) views.get(position);
            holder = (ViewHolder) convertView.getTag();
        } catch (IndexOutOfBoundsException e) {
            convertView = (ViewGroup) View.inflate(container.getContext(), R.layout.item_game, null);
            convertView.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContextUtil.toast_debug(v);
                }
            });
            holder = new ViewHolder();
            holder.item1 = convertView.findViewById(R.id.item1);
            holder.item2 = convertView.findViewById(R.id.item2);
            holder.item3 = convertView.findViewById(R.id.item3);
            convertView.setTag(holder);
            views.add(convertView);
        }

        if (position == pageCount - 1) {
            //最后一页，要根据比赛数量，来隐藏内容
            int visiblePage = games.size() - (pageCount - 1) * 3;
            for (int i = 0; i < 3; i++) {
                convertView.getChildAt(i).setVisibility(i < visiblePage ? View.VISIBLE : View.INVISIBLE);
            }
        }

        container.addView(convertView);
        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private class ViewHolder {
        View item1;
        View item2;
        View item3;
    }
}
