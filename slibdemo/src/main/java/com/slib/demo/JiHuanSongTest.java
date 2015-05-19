package com.slib.demo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.slib.demo.widget.PhotoItem;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.slib.demo.widget.PhotoItem.ImageHolder;


public class JiHuanSongTest extends Activity implements PhotoItem.OnAddClickedListener {
    PhotoItem mCurrent;
    private List<ImageHolder> imageHolders = new ArrayList<ImageHolder>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = new ListView(this);
        setContentView(listView);
        listView.setAdapter(new JHSAdapter());

        for (int i = 0; i < 20; i++) {
            imageHolders.add(new ImageHolder(new ArrayList<Bitmap>()));
        }
    }

    @Override
    public void onAdd(PhotoItem photoItem) {
        this.mCurrent = photoItem;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("crop", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    private class JHSAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return imageHolders.size();
        }

        @Override
        public Object getItem(int position) {
            return imageHolders.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PhotoItem item;
            if (convertView == null) {
                item = new PhotoItem(JiHuanSongTest.this);
                item.setOnAddClickedListener(JiHuanSongTest.this);
                item.setHolder(new ImageHolder(new ArrayList<Bitmap>()));
            } else {
                item = (PhotoItem) convertView;
            }
            item.setHolder((ImageHolder) getItem(position));
            return item;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            ContentResolver cr = this.getContentResolver();

            try {
                Bitmap bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
                mCurrent.addBitmap(bmp);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
