package com.sp.lib.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sp.lib.widget.material.MaterialLayout;

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
     *
     * @param activities
     */
    protected abstract void addTest(List<Class<? extends Activity>> activities);

    private class MyAdapter extends BaseAdapter implements View.OnClickListener{
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
            MaterialLayout layout;
            if (convertView == null) {
                layout = new MaterialLayout(STestActivity.this);
                layout.setMaterialBackground(Color.WHITE);
                layout.setMaterialWave(Color.LTGRAY);

                TextView textView = new TextView(STestActivity.this);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(17);
                textView.setPadding(10,15,10,15);
                layout.addView(textView);
                layout.setOnClickListener(this);
            } else {
                layout = (MaterialLayout) convertView;
            }
            ((TextView) layout.getChildAt(1)).setText(activities.get(position).getSimpleName());
            layout.setTag(position);
            return layout;
        }

        @Override
        public void onClick(View v) {
            int position= (int) v.getTag();
            startActivity(new Intent(STestActivity.this, activities.get(position)));
        }
    }
}
