package com.yjy998.ui.activity.my;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sp.lib.activity.album.PhotoAlbumActivity;
import com.sp.lib.activity.dialog.ListDialog;
import com.sp.lib.common.util.ImageUtil;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.admin.Assent;
import com.yjy998.common.ImageOptions;
import com.yjy998.http.Response;
import com.yjy998.http.YHttpClient;
import com.yjy998.http.YHttpHandler;
import com.yjy998.ui.activity.other.SecondActivity;
import com.yjy998.ui.pop.DatePickDialog;
import com.yjy998.ui.pop.PickCity;
import com.yjy998.ui.pop.YAlertDialogTwoButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChangeData extends SecondActivity {

    private ImageView avatarImage;
    private TextView nickText;
    private TextView genderText;
    private TextView birthDayText;
    private TextView liveText;
    private TextView educationText;
    private TextView handPasswordText;
    private EditText expEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_data);
        initialize();
    }

    private void initialize() {
        Assent assent = AppDelegate.getInstance().getUser().assent;
        if (assent==null){
            finish();
        }
        avatarImage = (ImageView) findViewById(R.id.avatarImage);
        nickText = (EditText) findViewById(R.id.nickText);
        genderText = (TextView) findViewById(R.id.genderText);
        birthDayText = (TextView) findViewById(R.id.birthDayText);
        liveText = (TextView) findViewById(R.id.liveText);
        educationText = (TextView) findViewById(R.id.educationText);
        handPasswordText = (TextView) findViewById(R.id.handPasswordText);
        expEdit = (EditText) findViewById(R.id.expEdit);
        findViewById(R.id.logout).setOnClickListener(this);
        findViewById(R.id.birthDayLayout).setOnClickListener(this);
        findViewById(R.id.genderLayout).setOnClickListener(this);
        findViewById(R.id.pickAddress).setOnClickListener(this);
        avatarImage.setOnClickListener(this);

        nickText.setText(assent.name);
//        genderText.setText(assent.);
//        birthDayText.setText(assent.);
//        educationText.setText(assent.);
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
            case R.id.birthDayLayout: {
                new DatePickDialog(this, new DatePickDialog.OnDatePicked() {
                    @Override
                    public void onPick(Calendar cal) {
                        birthDayText.setText(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
                    }
                }).show();
                break;
            }
            case R.id.genderLayout: {
                final String genders[] = getResources().getStringArray(R.array.genders);
                final ListDialog dialog = new ListDialog(this, genders);
                dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        genderText.setText(genders[position]);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            }
            case R.id.pickAddress: {
                new PickCity(this).show();
                break;
            }
            case R.id.logout: {
                YAlertDialogTwoButton dialog = new YAlertDialogTwoButton(this);
                dialog.setButton1(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        YHttpClient.getInstance().logout(new YHttpHandler() {
                            @Override
                            protected void onStatusCorrect(Response response) {
                                AppDelegate.getInstance().logout();
                            }
                        });
                    }
                });
                dialog.setButton2(R.string.cancel, null);
                dialog.setMessage(getString(R.string.confirm_logout));
                dialog.show();
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
