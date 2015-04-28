package com.yjy998.ui.activity.other;

import android.support.v4.app.Fragment;
import android.view.View;

public class BaseFragment extends Fragment {
    private String title;

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
}
