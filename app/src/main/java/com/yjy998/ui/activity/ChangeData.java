package com.yjy998.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sp.lib.activity.album.PhotoAlbumActivity;
import com.sp.lib.common.support.IntentFactory;
import com.sp.lib.common.util.ImageUtil;
import com.yjy998.R;
import com.yjy998.common.ImageOptions;

import java.io.IOException;

public class ChangeData extends SecondActivity {

    private ImageView toggle;
    private ImageView avatarImage;
    private TextView nickText;
    private TextView genderText;
    private TextView birthDayText;
    private TextView liveText;
    private TextView educationText;
    private TextView handPasswordText;
    private EditText expEdit;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_data);
        initialize();
    }

    private void initialize() {

        toggle = (ImageView) findViewById(R.id.toggle);
        avatarImage = (ImageView) findViewById(R.id.avatarImage);
        nickText = (TextView) findViewById(R.id.nickText);
        genderText = (TextView) findViewById(R.id.genderText);
        birthDayText = (TextView) findViewById(R.id.birthDayText);
        liveText = (TextView) findViewById(R.id.liveText);
        educationText = (TextView) findViewById(R.id.educationText);
        handPasswordText = (TextView) findViewById(R.id.handPasswordText);
        expEdit = (EditText) findViewById(R.id.expEdit);
        logout = (Button) findViewById(R.id.logout);
        avatarImage.setOnClickListener(this);
        ImageLoader.getInstance().displayImage("", avatarImage, ImageOptions.getAvatarInstance());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatarImage: {
                startActivityForResult(new Intent(this, PhotoAlbumActivity.class)
                        .putExtra(PhotoAlbumActivity.EXTRA_RETURN_DATA, true)
                        .putExtra(PhotoAlbumActivity.EXTRA_CAMERA_OUTPUT_HEIGHT, 200)
                        .putExtra(PhotoAlbumActivity.EXTRA_CAMERA_OUTPUT_WIDTH, 200)
                        , 100);
                break;
            }
        }

        super.onClick(v);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Bitmap bitmap = data.getExtras().getParcelable("data");
            avatarImage.setImageBitmap(ImageUtil.roundBitmap(bitmap, getResources().getDimensionPixelSize(R.dimen.avatarSize)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
