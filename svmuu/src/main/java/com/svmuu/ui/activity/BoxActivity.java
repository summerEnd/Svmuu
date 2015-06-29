package com.svmuu.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.svmuu.R;

public class BoxActivity extends SecondActivity {

    private CheckedTextView textBox;
    private CheckedTextView videoBox;
    private LinearLayout radioGroup;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);
        initialize();
    }

    private void initialize() {

        textBox = (CheckedTextView) findViewById(R.id.textBox);
        videoBox = (CheckedTextView) findViewById(R.id.videoBox);

        textBox.setOnClickListener(this);
        videoBox.setOnClickListener(this);

        radioGroup = (LinearLayout) findViewById(R.id.radioGroup);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.textBox:{
                videoBox.setChecked(false);
                textBox.setChecked(true);
                break;
            }
            case R.id.videoBox:{
                videoBox.setChecked(true);
                textBox.setChecked(false);
                break;
            }
        }

        super.onClick(v);
    }
}
