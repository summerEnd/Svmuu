package com.sp.lib.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public abstract class STestActivity extends ListActivity {
   private List<Class<? extends Activity>> activities = new ArrayList<Class<? extends Activity>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addTest(activities);
        setListAdapter(new MyAdapter());
    }

    /**
     * add your test Here
     * @param activities
     */
    protected abstract void addTest(List<Class<? extends Activity>> activities);

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        startActivity(new Intent(this, activities.get(position)));
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return activities.size();
        }

        @Override
        public Object getItem(int position) {
            return activities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
            }
            ((TextView) convertView).setText(activities.get(position).getSimpleName());

            return convertView;
        }
    }
}
