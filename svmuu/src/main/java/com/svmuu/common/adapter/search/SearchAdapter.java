package com.svmuu.common.adapter.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.svmuu.R;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.adapter.BaseAdapter;
import com.svmuu.common.adapter.BaseHolder;
import com.svmuu.common.adapter.search.SearchHolder.SearchHistory;
import com.svmuu.common.adapter.search.SearchHolder.SearchHistoryTitle;
import com.svmuu.common.adapter.search.SearchHolder.SearchResult;
import com.svmuu.common.entity.History;
import com.svmuu.common.entity.Search;

import java.util.List;

public class SearchAdapter extends BaseAdapter<History,SearchHolder> {

    public static final int TYPE_SEARCH = 1;
    public static final int TYPE_SEARCH_HISTORY = 2;
    public static final int TYPE_SEARCH_CLEAR_HISTORY = 3;
    public static final int TYPE_SEARCH_HISTORY_TITLE = 4;
    private List<History> histories;
    private List<Search> searches;
    private boolean showHistory;
    private DisplayImageOptions options;


    private BaseHolder.OnItemListener listener;


    public SearchAdapter(@NonNull Context context) {
        super(context);
        options= ImageOptions.getRoundCorner(5);
    }

    public boolean isShowHistory() {
        return showHistory;
    }

    public void setListener(BaseHolder.OnItemListener listener) {
        this.listener = listener;
    }

    public void showHistory(List<History> histories) {
        this.histories = histories;
        showHistory = true;
        notifyDataSetChanged();
    }

    public void showResult(List<Search> masters){
        this.searches =masters;
        showHistory=false;
        notifyDataSetChanged();
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SearchHolder holder;
        switch (viewType) {

            case TYPE_SEARCH_HISTORY: {
                holder = new SearchHistory(getInflater().inflate(R.layout.search_history, parent, false));
                break;
            }
            case TYPE_SEARCH_HISTORY_TITLE:
            case TYPE_SEARCH_CLEAR_HISTORY: {
                View inflate = getInflater().inflate(R.layout.hisitory_item_search_title, parent, false);
                holder = new SearchHistoryTitle(inflate);
                break;
            }
            default:
                holder = new SearchResult(getInflater().inflate(R.layout.search_result, parent, false));
        }
        if (listener!=null)holder.setListener(listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, int position) {
        switch (getItemViewType(position)) {

            case TYPE_SEARCH_HISTORY: {
                SearchHistory h = (SearchHistory) holder;
                int index = position-1;//减1是减去列表的标题
                History history = histories.get(index);
                h.tv_search.setText(history.name);
                break;
            }
            case TYPE_SEARCH_CLEAR_HISTORY:
            case TYPE_SEARCH_HISTORY_TITLE: {
                SearchHistoryTitle h = (SearchHistoryTitle) holder;

                if (position==0){
                    h.itemView.setEnabled(false);
                    h.title.setText(getContext().getString(R.string.search_history));
                }else{
                    h.itemView.setEnabled(true);
                    h.title.setText(getContext().getString(R.string.clear_search_history));
                }

                break;
            }

            default: {
                if (searches==null||searches.size()==0){
                    return;
                }
                SearchResult h = (SearchResult) holder;
                Search result= searches.get(position);
                ImageLoader.getInstance().displayImage(result.uface, h.avatarImage, options);
                h.nickText.setText(result.unick);
                h.tvcircleNo.setText(getString(R.string.circle_no_s, result.uid));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (showHistory) {
            if (position == 0) {
                type = TYPE_SEARCH_HISTORY_TITLE;
            } else if (position == getItemCount() - 1) {
                type = TYPE_SEARCH_CLEAR_HISTORY;
            } else {
                type = TYPE_SEARCH_HISTORY;
            }

        } else {
            type = TYPE_SEARCH;
        }


        return type;
    }

    @Override
    public int getItemCount() {
        if (showHistory){
            return histories.size()+2;
        }
        return searches.size();
    }


}
