package com.slib.demo.material;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.slib.demo.SLIBTest;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.widget.material.MaterialLayout;

public class MaterialGridView extends SLIBTest {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GridView gridView = new GridView(this);
        setContentView(gridView);
        gridView.setNumColumns(3);
        gridView.setHorizontalSpacing(2);
        gridView.setVerticalSpacing(2);
        gridView.setAdapter(new MyAdapter());
    }

    class MyAdapter extends BaseAdapter implements View.OnClickListener {

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MaterialLayout layout;
            if (convertView == null) {
                layout = new MaterialLayout(MaterialGridView.this);
                layout.setMaterialBackground(Color.WHITE);
                layout.setMaterialWave(Color.LTGRAY);

                TextView textView = new TextView(MaterialGridView.this);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(17);
                textView.setPadding(10, 45, 10, 45);
                layout.addView(textView);
                layout.setOnClickListener(this);
            } else {
                layout = (MaterialLayout) convertView;
            }
            ((TextView) layout.getChildAt(1)).setText("position:" + position);
            layout.setTag(position);
            return layout;
        }

        @Override
        public void onClick(View v) {
            ContextUtil.toast(v.getTag());
        }
    }
}
