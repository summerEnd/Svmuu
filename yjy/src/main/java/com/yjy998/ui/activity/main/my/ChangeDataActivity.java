package com.yjy998.ui.activity.main.my;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sp.lib.activity.DEBUGActivity;
import com.sp.lib.activity.dialog.ListDialog;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.ImageUtil;
import com.sp.lib.common.util.JsonUtil;
import com.yjy998.AppDelegate;
import com.yjy998.R;
import com.yjy998.common.Constant;
import com.yjy998.common.entity.User;
import com.yjy998.common.entity.UserInfo;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.common.util.ImageOptions;
import com.yjy998.ui.activity.admin.LockActivity;
import com.yjy998.ui.activity.base.SecondActivity;
import com.yjy998.ui.pop.DatePickDialog;
import com.yjy998.ui.pop.PickCity;
import com.yjy998.ui.pop.YAlertDialogTwoButton;
import com.yjy998.ui.pop.YProgressDialog;

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
        UserInfo assent = AppDelegate.getInstance().getUser().userInfo;
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
        refresh();
    }

    void refresh() {
        UserInfo assent = AppDelegate.getInstance().getUser().userInfo;
        String r_location = assent.r_location;
        if (TextUtils.isEmpty(r_location)) {
            mProvince = "";
            mCity = "";
        } else {
            String location[] = r_location.split(",");
            if (location.length == 0) {
                mProvince = r_location;
                mCity = "";
                return;
            }
            mProvince = location[0];
            if (location.length > 1) {
                mCity = location[1];
            } else {
                mCity = "";
            }
        }

        String genders[] = getResources().getStringArray(R.array.genders);
        liveText.setText(mProvince);
        if (!TextUtils.isEmpty(mCity)) {
            liveText.append("," + mCity);
        }
        nickText.setText(assent.unick);
        genderText.setText("0".equals(assent.gender) ? genders[0] : genders[1]);
        birthDayText.setText(assent.birthday);
        educationText.setText(assent.education_degree);
        expEdit.setText(assent.invest_exper);
        ImageLoader.getInstance().displayImage(assent.uface, avatarImage, ImageOptions.getAvatarInstance());
    }

    @Override
    protected void onResume() {
        super.onResume();
        handPasswordText.setText(LockActivity.isLockEnabled() ? R.string.setAlready : R.string.notSet);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_change_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done: {
                updateInfo();
                return true;
            }
            case R.id.debug: {
                startActivity(new Intent(this, DEBUGActivity.class));
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);

        }
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
                                liveText.setText(mProvince + "," + city);
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
                if (LockActivity.isLockEnabled()) {
                    String[] items = new String[]{getString(R.string.close_psw), getString(R.string.change_hand_psw)};
                    final ListDialog dialog = new ListDialog(this, items);
                    dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                case 0:
                                    startActivity(new Intent(ChangeDataActivity.this, SetGesturePassword.class)
                                                    .putExtra(SetGesturePassword.EXTRA_DO_WHAT, SetGesturePassword.DO_CLOSE)
                                    );

                                    break;
                                case 1:
                                    startActivity(new Intent(ChangeDataActivity.this, SetGesturePassword.class)
                                                    .putExtra(SetGesturePassword.EXTRA_DO_WHAT, SetGesturePassword.DO_CHANGE)
                                    );
                                    break;
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    String[] items = new String[]{getString(R.string.open_psw)};
                    final ListDialog dialog = new ListDialog(this, items);
                    dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                case 0:
                                    startActivity(new Intent(ChangeDataActivity.this, SetGesturePassword.class)
                                                    .putExtra(SetGesturePassword.EXTRA_DO_WHAT, SetGesturePassword.DO_OPEN)
                                    );
                                    break;
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }

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
        request.put("email", "-");
        request.put("education-degree", education);
        request.put("invest-exper", invest_exp);
        YHttpClient.getInstance().post(request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                getUserInfo();
            }

            @Override
            public Dialog onCreateDialog() {
                YProgressDialog dialog=new YProgressDialog(ChangeDataActivity.this);
                dialog.setMessage(getString(R.string.saving));
                return dialog;
            }
        });
    }

    void getUserInfo() {
        SRequest request = new SRequest();

        YHttpClient.getInstance().getByMethod(this, "/h5/account/assentinfo", request, new YHttpHandler(false) {
            @Override
            protected void onStatusCorrect(Response response) {

                try {
                    AppDelegate.getInstance().setUser(JsonUtil.get(response.data, User.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                refresh();
            }
        });
    }
}
