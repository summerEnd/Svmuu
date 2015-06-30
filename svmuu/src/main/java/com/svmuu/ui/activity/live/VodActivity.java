package com.svmuu.ui.activity.live;

import android.os.Bundle;
import android.view.View;

import com.gensee.view.GSVideoView;
import com.svmuu.R;
import com.svmuu.common.VodManager;
import com.svmuu.ui.BaseActivity;

public class VodActivity extends BaseActivity {
    VodManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vod);
        manager = VodManager.getInstance(this,(GSVideoView) findViewById(R.id.gsView));

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.start("30d998bdd65b4a2bb6c405cad9d8dee5", "964786");
            }
        });
    }

    @Override
    public void onBackPressed() {
        manager.release();
        super.onBackPressed();
    }
}
