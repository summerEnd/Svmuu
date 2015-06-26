package com.svmuu.common.adapter.search;

import android.view.View;
import android.widget.TextView;

import com.sp.lib.common.util.ContextUtil;
import com.svmuu.R;
import com.svmuu.common.adapter.BaseHolder;

public class SearchHolder extends BaseHolder {
    public SearchHolder(View itemView) {
        super(itemView);
    }

    /**
     * 搜索结果
     */
    public static class SearchResult extends SearchHolder {
        public SearchResult(View itemView) {
            super(itemView);
        }
    }

    /**
     * 搜索历史纪录
     */
    public static class SearchHistory extends SearchHolder {
        TextView tv_search;
        public SearchHistory(View itemView) {
            super(itemView);
            tv_search= (TextView) findViewById(R.id.tv_search);
        }
    }

    /**
     * 搜索历史纪录标题\清除搜索纪录
     */
    public static class SearchHistoryTitle extends SearchHolder {
        TextView title;
        public SearchHistoryTitle(View itemView) {
            super(itemView);
            title= (TextView) findViewById(R.id.title);
        }

        public void setTitle(){

        }

        @Override
        public void onClick(View v) {
            super.onClick(v);

        }
    }

}
