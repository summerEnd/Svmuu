package com.svmuu.common.adapter.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.svmuu.R;
import com.svmuu.common.adapter.BaseAdapter;
import com.svmuu.common.adapter.search.SearchHolder.SearchHistory;
import com.svmuu.common.adapter.search.SearchHolder.SearchHistoryTitle;
import com.svmuu.common.adapter.search.SearchHolder.SearchResult;
import com.svmuu.common.entity.CircleMaster;
import com.svmuu.common.entity.History;

import java.util.List;

public class SearchAdapter extends BaseAdapter<SearchHolder> {

    public static final int TYPE_SEARCH = 1;
    public static final int TYPE_SEARCH_HISTORY = 2;
    public static final int TYPE_SEARCH_CLEAR_HISTORY = 3;
    public static final int TYPE_SEARCH_HISTORY_TITLE = 4;
    private List<History> histories;
    private List<CircleMaster> masters;
    private boolean showHistory;

    public SearchAdapter(@NonNull Context context) {
        super(context);
    }

    public void showHistory(List<History> histories) {
        this.histories = histories;
        showHistory = true;
        notifyDataSetChanged();
    }

    public void showResult(List<CircleMaster> masters){
        this.masters=masters;
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

        return holder;
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, int position) {
        switch (getItemViewType(position)) {

            case TYPE_SEARCH_HISTORY: {
                SearchHistory h = (SearchHistory) holder;
                History history = histories.get(position-1);//减1是减去列表的标题
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
                SearchResult h = (SearchResult) holder;

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
        return 10;
    }
}
