package com.svmuu.ui.activity.box;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;

import com.svmuu.R;
import com.svmuu.ui.activity.SecondActivity;
import com.svmuu.ui.activity.live.PlayFragment;

public class BoxDetailActivity extends SecondActivity {

    public static final String EXTRA_ID = "id";//点播id
    public static final String EXTRA_TOKEN = "token";//加入口令
    public static final String EXTRA_SUBJECT = "subject";//视频主题
    public static final String EXTRA_IS_BOX = "isBox";//是否为宝盒
    public static final String EXTRA_CIRCLE_NAME = "name";//圈名

    boolean isBox;
    String id;
    String token;
    String subject;
    String name;
    PlayFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_detail);
        isBox = getIntent().getBooleanExtra(EXTRA_IS_BOX, false);
        id = getIntent().getStringExtra(EXTRA_ID);
        token = getIntent().getStringExtra(EXTRA_TOKEN);
        subject = getIntent().getStringExtra(EXTRA_SUBJECT);

        fragment = new PlayFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.videoContainer, fragment).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(EXTRA_IS_BOX, isBox);
        outState.putString(EXTRA_ID, id);
        outState.putString(EXTRA_TOKEN, token);
        outState.putString(EXTRA_SUBJECT, subject);
        outState.putString(EXTRA_CIRCLE_NAME, name);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isBox = savedInstanceState.getBoolean(EXTRA_IS_BOX);
        id = savedInstanceState.getString(EXTRA_ID);
        token = savedInstanceState.getString(EXTRA_TOKEN);
        subject = savedInstanceState.getString(EXTRA_SUBJECT);
        name = savedInstanceState.getString(EXTRA_CIRCLE_NAME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fragment.playVod(id, token);
        fragment.setSubject(subject);
        if (isBox) {
            setTitle(R.string.title_activity_box_detail);
        } else {
            setTitle(name);
        }
    }

    @Override
    public void onBackPressed() {
        fragment.onActivityClose();
    }
}
