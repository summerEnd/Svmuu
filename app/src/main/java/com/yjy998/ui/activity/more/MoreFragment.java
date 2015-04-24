package com.yjy998.ui.activity.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yjy998.R;
import com.yjy998.ui.activity.other.BaseFragment;

public class MoreFragment extends BaseFragment {

    View layout;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_more, container, false);
        listView = (ListView) layout.findViewById(R.id.listView);
        return layout;
    }

}
