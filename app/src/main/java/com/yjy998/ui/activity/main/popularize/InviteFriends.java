package com.yjy998.ui.activity.main.popularize;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sp.lib.common.support.IntentFactory;
import com.yjy998.R;
import com.yjy998.ui.activity.base.SecondActivity;

public class InviteFriends extends SecondActivity {

    private ImageView topImage;
    private ImageView codeImage;
    private TextView url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        initialize();
    }

    private void initialize() {

        topImage = (ImageView) findViewById(R.id.topImage);
        codeImage = (ImageView) findViewById(R.id.codeImage);
        url = (TextView) findViewById(R.id.url);
        findViewById(R.id.copy).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.copy: {
                ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData data = ClipData.newPlainText("label", url.getText().toString());
                manager.setPrimaryClip(data);
                break;
            }
            case R.id.share: {
                Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
                intent.setType("text/plain"); // 分享发送的数据类型
                //        intent.setPackage(context.getPackageName());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.invite_friends)); // 分享的主题
                intent.putExtra(Intent.EXTRA_TEXT, url.getText().toString()); // 分享的内容
                startActivity(Intent.createChooser(intent, getString(R.string.invite_friends)));
                break;
            }
        }
    }
}
