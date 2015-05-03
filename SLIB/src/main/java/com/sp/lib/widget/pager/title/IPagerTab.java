package com.sp.lib.widget.pager.title;

import android.view.View;
import android.widget.Checkable;

public interface IPagerTab {
    public View getView();

    public void setTabSelect(boolean selected);

    public boolean isTabSelected();
}
