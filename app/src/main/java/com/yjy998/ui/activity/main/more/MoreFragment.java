package com.yjy998.ui.activity.main.more;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yjy998.R;
import com.yjy998.ui.activity.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class MoreFragment extends BaseFragment {

    View layout;
    ListView listView;
    List<Problem> problems = new ArrayList<Problem>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_more, container, false);
        listView = (ListView) layout.findViewById(R.id.list);

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView.setAdapter(new ProblemAdapter());
    }

    private class Problem {
        String title;
        String content;
        boolean isRead;
    }

    private class ProblemAdapter extends BaseAdapter {

        private int READ_COLOR = 0xffe42d42;
        private int NORMAL_COLOR = 0xFF333333;

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public Object getItem(int position) {
            return "";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv;
            if (convertView == null) {
                tv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.textview,null);
            } else {
                tv = (TextView) convertView;
            }
            tv.setText("1、什么是易交易？");

            if (position == 3) {
                tv.setTextColor(READ_COLOR);
            } else {
                tv.setTextColor(NORMAL_COLOR);
            }

            return tv;
        }
    }

}
