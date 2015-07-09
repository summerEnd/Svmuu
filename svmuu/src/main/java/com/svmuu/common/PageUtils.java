package com.svmuu.common;

import android.support.annotation.NonNull;

import com.sp.lib.common.util.ContextUtil;
import com.svmuu.R;

import java.util.List;

/**
 * 分页工具
 */
public class PageUtils {
    int page = 1;
    private  boolean isRefresh=true;
    /**
     * @param data    原来总的数据
     * @param newData 新的数据
     * @return true 添加新数据成功 false 没有新数据
     */
    public <T> boolean addNewPage(@NonNull List<T> data, List<T> newData) {
        if (newData == null || newData.size() == 0) {
            ContextUtil.toast(R.string.data_load_done);
            return false;
        }
        if (isRefresh) {
            data.clear();
            page = 1;
        } else {
            page++;
        }
        ContextUtil.toast(page);

        data.addAll(newData);
        return true;
    }

    public int getPage() {
        return page;
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
