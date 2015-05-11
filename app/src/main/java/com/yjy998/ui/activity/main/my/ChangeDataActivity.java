package com.yjy998.ui.activity.main.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sp.lib.activity.album.PhotoAlbumActivity;
import com.sp.lib.activity.dialog.ListDialog;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.ImageUtil;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.common.entity.Assent;
import com.yjy998.common.util.ImageOptions;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.base.SecondActivity;
import com.yjy998.ui.pop.DatePickDialog;
import com.yjy998.ui.pop.PickCity;
import com.yjy998.ui.pop.YAlertDialogTwoButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ChangeDataActivity extends SecondActivity {

    private ImageView avatarImage;
    private TextView nickText;
    private TextView genderText;
    private TextView birthDayText;
    private TextView liveText;
    private TextView educationText;
    private TextView handPasswordText;
    private EditText expEdit;
    String mProvince;
    String mCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_data);
        initialize();
    }

    private void initialize() {
        Assent assent = AppDelegate.getInstance().getUser().userInfo;
        if (assent == null) {
            finish();
            return;
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
        handPasswordText.setOnClickListener(this);
        avatarImage.setOnClickListener(this);

        if (TextUtils.isEmpty(assent.r_location)) {
            mProvince = "";
            mCity = "";
        } else {
            String location[] = assent.r_location.split(",");
            mProvince = location[0];
            if (location.length >= 2) {
                mCity = location[1];
            } else {
                mCity = "";
            }
        }

        String genders[] = getResources().getStringArray(R.array.genders);
        liveText.setText(mProvince + "," + mCity);

        nickText.setText(assent.name);
        genderText.setText(assent.gender.equals("0")?genders[0]:genders[1]);
        birthDayText.setText(assent.birthday);
        educationText.setText(assent.education_degree);
        ImageLoader.getInstance().displayImage(assent.uface, avatarImage, ImageOptions.getAvatarInstance());


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.avatarImage: {
//                startActivityForResult(new Intent(this, PhotoAlbumActivity.class)
//                        .putExtra(PhotoAlbumActivity.EXTRA_RETURN_DATA, true)
//                        .putExtra(PhotoAlbumActivity.EXTRA_CAMERA_OUTPUT_HEIGHT, 200)
//                        .putExtra(PhotoAlbumActivity.EXTRA_CAMERA_OUTPUT_WIDTH, 200)
//                        , 100);
                break;
            }
            case R.id.birthDayLayout: {
                new DatePickDialog(this, new DatePickDialog.OnDatePicked() {
                    @Override
                    public void onPick(Calendar cal) {
                        birthDayText.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime()));
                        updateInfo();
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
                        genderText.setTag(position);
                        dialog.dismiss();
                        updateInfo();
                    }
                });
                dialog.show();
                break;
            }
            case R.id.pickAddress: {
                new PickCity(this)
                        .setOnSelectListener(new PickCity.OnSelectListener() {
                            @Override
                            public void onSelect(String province, String city) {
                                mProvince = province;
                                mCity = city;
                                updateInfo();
                            }
                        })
                        .show();
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
                                finish();
                            }
                        });
                    }
                });
                dialog.setButton2(R.string.cancel, null);
                dialog.setMessage(getString(R.string.confirm_logout));
                dialog.show();
                break;
            }
            case R.id.handPasswordText: {
                startActivity(new Intent(this, SetGesturePassword.class));
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

    /**
     * 参数：sex（性别，0男1女），unick（昵称），birthday（生日），mProvince（省），city（市），email（邮箱），education-degree（教育程度），invest-exper（投资经验）
     */
    public void updateInfo() {

        String sex = genderText.getTag() + "";
        String unick = nickText.getText().toString();
        String birthday = birthDayText.getText().toString();
        String education = educationText.getText().toString();
        String invest_exp = expEdit.getText().toString();

        SRequest request = new SRequest("http://www.yjy998.com/account/info");
        request.put("sex", sex);
        request.put("unick", unick);
        request.put("birthday", birthday);
        request.put("province", mProvince);
        request.put("city", mCity);
        //request.put("email", "");
        request.put("education-degree", education);
        request.put("invest-exper", invest_exp);
        YHttpClient.getInstance().post(request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {

            }
        });
    }

}
