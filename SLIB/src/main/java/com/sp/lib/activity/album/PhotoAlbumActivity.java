package com.sp.lib.activity.album;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sp.lib.R;
import com.sp.lib.activity.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static com.sp.lib.activity.album.PhotoDirAdapter.PhotoDirInfo;

/**
 *
 */
public class PhotoAlbumActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private final String TAG = "PhotoAlbumActivity";
    GridView grid;
    LinearLayout bottom_ll;

    /**
     * 图片目录和图片url集合
     */
    private HashMap<String, LinkedList<String>> urlsMap = new HashMap<String, LinkedList<String>>();
    private final int OPEN_CAMERA = 100;
    private final int CROP_IMAGE = 101;
    /**
     * Intent参数，拍照选的保存路径，String类型。如果不传，将不保存。
     */
    public static String EXTRA_CAMERA_OUTPUT_PATH = "camera_out_path";
    /**
     * 输出图片的高度，单位dp
     */
    public static String EXTRA_CAMERA_OUTPUT_HEIGHT = "camera_out_height";
    /**
     * 输出图片的宽度，单位dp
     */
    public static String EXTRA_CAMERA_OUTPUT_WIDTH = "camera_out_width";

    /**
     * 相机的输出uri
     */
    private Uri cameraUri;

    private int outPut_height;
    private int outPut_width;
    private LinkedList<String> curAlbumList;
    PhotoGridAdapter pictureAdapter;
    private PhotoDirWindow dirWindow;
    private final String ALL = "全部";
    private final String OTHER = "其他";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_albumn);
        grid = (GridView) findViewById(R.id.grid);
        bottom_ll = (LinearLayout) findViewById(R.id.bottom_ll);
        loadPictures();
        initParams();
        bottom_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow();
            }
        });
    }

    /**
     * 初始化参数
     */
    void initParams() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        String camera_output = getIntent().getStringExtra(EXTRA_CAMERA_OUTPUT_PATH);
        outPut_height = (int) (getIntent().getIntExtra(EXTRA_CAMERA_OUTPUT_HEIGHT, 30) * dm.density);
        outPut_width = (int) (getIntent().getIntExtra(EXTRA_CAMERA_OUTPUT_WIDTH, 30) * dm.density);
        if (!TextUtils.isEmpty(camera_output))
            cameraUri = Uri.fromFile(new File(camera_output));
        else
            cameraUri = Uri.fromFile(new File(getCacheDir(), "album"));
    }

    /**
     * 加载图片
     */
    private void loadPictures() {

        String[] projection = {
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DATE_MODIFIED
        };
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null,
                MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC");

        LinkedList<String> totalList = new LinkedList<String>();
        //全部
        urlsMap.put(ALL, totalList);
        while (cursor.moveToNext()) {
            String url = cursor.getString(0);
            String dirName = url.replace("/mnt", "")
                    .replace("/sdcard/", "")
                    .replace("/ext_sdcard/", "")
                    .replace("/sdcard0/", "");
            //获取目录名称
            try {
                dirName = dirName.substring(0, dirName.indexOf('/'));
            } catch (Exception e) {
                dirName = OTHER;
            }
            //从map中获取一个 list，如果没有就创建一个
            LinkedList<String> tempList = urlsMap.get(dirName);
            if (tempList == null) {
                tempList = new LinkedList<String>();
                urlsMap.put(dirName, tempList);
            }
            tempList.add(url);
            totalList.add(url);
        }

        if (Build.VERSION.SDK_INT < 14) {
            //在14以上版本，cursor会自动关闭，如果手动关闭在resume的时候会抛出异常。
            cursor.close();
        }
        pictureAdapter = new PhotoGridAdapter(this, urlsMap.get(ALL));
        grid.setAdapter(pictureAdapter);
        grid.setOnItemClickListener(this);
    }

    /**
     * 创建popupWindow
     *
     * @return
     */
    private void showWindow() {
        if (dirWindow == null) {
            //收集相册信息
            ArrayList<PhotoDirInfo> dirs = new ArrayList<PhotoDirInfo>();
            for (String dirName : urlsMap.keySet()) {
                PhotoDirInfo info = new PhotoDirInfo();
                info.dirName = dirName;
                info.photos = urlsMap.get(dirName);
                dirs.add(info);
            }

            dirWindow = new PhotoDirWindow(this, dirs) {
                @Override
                protected void onSelected(PhotoDirInfo info) {
                    //获取目录对应的图片列表
                    String dirName = info.dirName;
                    curAlbumList = urlsMap.get(dirName);
                    //设置标题为目录名称
                    setTitle(dirName);
                    pictureAdapter.setData(curAlbumList);
                    dirWindow.dismiss();
                }
            };
        }
        dirWindow.showWithAnimation(bottom_ll);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            //打开相机拍照
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            }
            startActivityForResult(intent, OPEN_CAMERA);
        } else {//选择照片，返回照片Uri
            File selected = new File(curAlbumList.get(position - 1));
            cropImage(Uri.fromFile(selected));
        }
    }

    /**
     * 返回图片uri
     *
     * @param uri
     */
    private void returnUri(Uri uri) {
        Intent data = new Intent();
        data.setData(uri);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
        } else if (requestCode == OPEN_CAMERA) {
            Log.i(TAG, "CROP_IMAGE output:" + cameraUri);
            cropImage(cameraUri);
        } else if (requestCode == CROP_IMAGE) {
            returnUri(cameraUri);
        }
    }

    /**
     * 剪裁图片
     *
     * @param uri
     */
    private void cropImage(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", outPut_width);
        intent.putExtra("aspectY", outPut_height);
        intent.putExtra("outputX", outPut_width);
        intent.putExtra("outputY", outPut_height);
        intent.putExtra("output", cameraUri);// 保存到原文件
        intent.putExtra("outputFormat", "JPEG");// 返回格式
        intent.putExtra("return-data", false);
        startActivityForResult(intent, CROP_IMAGE);
    }
}