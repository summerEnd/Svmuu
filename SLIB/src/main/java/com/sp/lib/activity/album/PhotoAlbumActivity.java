package com.sp.lib.activity.album;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sp.lib.R;
import com.sp.lib.activity.SlibActivity;
import com.sp.lib.support.util.ContextUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import static com.sp.lib.activity.album.PhotoDirAdapter.PhotoDirInfo;

/**
 * <pre>
 * example;
 * startActivityForResult
 * </pre>
 */
public class PhotoAlbumActivity extends SlibActivity implements AdapterView.OnItemClickListener {

    GridView grid;
    LinearLayout selectAlbum;

    /**
     * 图片目录和图片url集合
     */
    private HashMap<String, LinkedList<String>> mAlbumListMap = new HashMap<String, LinkedList<String>>();
    private final int CAPTURE_IMAGE = 100;
    private final int CROP_IMAGE = 101;

    /**
     * 输出图片的高度
     */
    public static String EXTRA_CAMERA_OUTPUT_HEIGHT = "camera_out_height";
    /**
     * 输出图片的宽度
     */
    public static String EXTRA_CAMERA_OUTPUT_WIDTH = "camera_out_width";

    /**
     * boolean类型，true 返回图片Uri，false 不返回
     */
    public static String EXTRA_RETURN_DATA = "return-data";


    private int outPut_height;
    private int outPut_width;
    /**
     * 当前相册列表
     */
    private LinkedList<String> mAlbumList;
    PhotoGridAdapter photoAdapter;
    private PhotoDirWindow mPhotoDirWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_albumn);
        initParams();
    }

    /**
     * 初始化参数
     */
    void initParams() {
        outPut_height = getIntent().getIntExtra(EXTRA_CAMERA_OUTPUT_HEIGHT, 30);
        outPut_width = getIntent().getIntExtra(EXTRA_CAMERA_OUTPUT_WIDTH, 30);
        grid = (GridView) findViewById(R.id.grid);
        selectAlbum = (LinearLayout) findViewById(R.id.bottom_ll);
        selectAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow();
            }
        });
        loadPictures();
    }

    private File createFile(String fileName) {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (dir == null) {
            ContextUtil.toast("sdcard is not available");
            dir = getFilesDir();
        }

        return new File(dir, fileName);
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
        mAlbumList = totalList;
        //全部
        String dir_all = getString(R.string.all);
        //其他
        String dir_other = getString(R.string.other);


        mAlbumListMap.put(dir_all, totalList);
        while (cursor.moveToNext()) {
            String url = cursor.getString(0);
            String dirName = url.replace("/sdcard/", "").replace("/mnt", "")
                    .replace("/ext_sdcard/", "")
                    .replace("/sdcard0/", "");
            //获取目录名称
            try {
                dirName = dirName.substring(0, dirName.indexOf('/'));
            } catch (Exception e) {
                dirName = dir_other;
            }
            //从map中获取一个 list，如果没有就创建一个
            LinkedList<String> tempList = mAlbumListMap.get(dirName);
            if (tempList == null) {
                tempList = new LinkedList<String>();
                mAlbumListMap.put(dirName, tempList);
            }
            tempList.add(url);
            totalList.add(url);
        }
        photoAdapter = new PhotoGridAdapter(PhotoAlbumActivity.this, mAlbumListMap.get(getString(R.string.all)));
        grid.setAdapter(photoAdapter);
        grid.setOnItemClickListener(this);
        if (Build.VERSION.SDK_INT < 14) {
            //在14以上版本，cursor会自动关闭，如果手动关闭在resume的时候会抛出异常。
            cursor.close();
        }

    }

    /**
     * 创建popupWindow
     */
    private void showWindow() {
        if (mPhotoDirWindow == null) {
            //收集相册信息
            ArrayList<PhotoDirInfo> dirs = new ArrayList<PhotoDirInfo>();
            for (String dirName : mAlbumListMap.keySet()) {
                PhotoDirInfo info = new PhotoDirInfo();
                info.dirName = dirName;
                info.photos = mAlbumListMap.get(dirName);
                dirs.add(info);
            }

            mPhotoDirWindow = new PhotoDirWindow(this, dirs) {
                @Override
                protected void onSelected(PhotoDirInfo info) {
                    //获取目录对应的图片列表
                    String dirName = info.dirName;
                    mAlbumList = mAlbumListMap.get(dirName);
                    //设置标题为目录名称
                    setTitle(dirName);
                    photoAdapter.setData(mAlbumList);
                    mPhotoDirWindow.dismiss();
                }
            };
        }
        mPhotoDirWindow.showWithAnimation(selectAlbum);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            //打开相机拍照
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

            Uri cameraUri = Uri.fromFile(createFile("SLIB_"+sdf.format(new Date())));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);

            startActivityForResult(intent, CAPTURE_IMAGE);
        } else {//选择照片，返回照片Uri
            File selected = new File(mAlbumList.get(position - 1));
            cropImage(Uri.fromFile(selected));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, R.string.fail, Toast.LENGTH_SHORT).show();
        } else if (requestCode == CAPTURE_IMAGE) {
            //拍照返回
        } else if (requestCode == CROP_IMAGE) {
            //裁剪返回
            setResult(RESULT_OK, data);
            finish();
        }
    }

    /**
     * 剪裁图片
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
//        intent.putExtra("output", cameraUri);// 保存到原文件
        intent.putExtra("outputFormat", "JPEG");// 返回格式
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_IMAGE);
    }
}