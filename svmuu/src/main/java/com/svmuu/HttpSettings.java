package com.svmuu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.svmuu.common.http.HttpManager;


public class HttpSettings extends ActionBarActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_settings);
        initialize();
    }


    private void initialize() {
        sp = getSharedPreferences("http", MODE_PRIVATE);
        ListView list = (ListView) findViewById(R.id.list);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, new String[]{"dev", "m-dev", "dev-test", "mtests", "www"}));
        list.setItemChecked(HttpManager.getType(), true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HttpManager.setType(position);
                sp.edit().putInt("position", position).apply();
            }
        });

    }
}
