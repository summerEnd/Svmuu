package com.yjy998.ui.activity.base;

import android.support.v4.app.Fragment;
import android.view.View;

public class BaseFragment extends Fragment {
    private String title;
    private boolean doRefreshWhenCreated;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 这个方法应该在{@link android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle) onViewCreated }之后调用，否则会返回null
     */
    public View findViewById(int id) {
        return getView().findViewById(id);
    }

    /**
     * 刷新
     */
    public void refresh() {
    }

    public boolean isDoRefreshWhenCreated() {
        return doRefreshWhenCreated;
    }

    public void setDoRefreshWhenCreated(boolean doRefreshWhenCreated) {
        this.doRefreshWhenCreated = doRefreshWhenCreated;
    }
}
