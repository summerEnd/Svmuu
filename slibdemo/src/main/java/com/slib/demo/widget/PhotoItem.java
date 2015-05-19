package com.slib.demo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sp.lib.widget.GridLayout;
import com.svmuu.slibdemo.R;

import java.util.List;

public class PhotoItem extends FrameLayout {
    GridLayout gridLayout;
    TextView title;
    private ImageHolder holder;
    OnAddClickedListener onAddClickedListener;

    public PhotoItem(Context context) {
        this(context, null);
    }

    public PhotoItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.activity_ji_huan_song_test, null);
        title = (TextView) layout.findViewById(R.id.title);
        gridLayout = (GridLayout) layout.findViewById(R.id.grid);
        int count = gridLayout.getChildCount();
        gridLayout.findViewById(R.id.addImage).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAddClickedListener != null) {
                    onAddClickedListener.onAdd(PhotoItem.this);
                }
            }
        });
        addView(layout);
    }

    public ImageHolder getHolder() {
        return holder;
    }

    public void setHolder(ImageHolder holder) {
        this.holder = holder;
        refreshImage();
    }

    private void refreshImage() {
        int count = gridLayout.getChildCount();
        List<Bitmap> images = holder.images;
        int imageNumber = images == null ? 0 : images.size();
        for (int i = 0; i < count; i++) {
            ImageView imageView = (ImageView) gridLayout.getChildAt(i);
            if (i < imageNumber) {
                imageView.setImageBitmap(images != null ? images.get(i) : null);
                imageView.setVisibility(VISIBLE);
            } else if (i != count - 1) {
                imageView.setVisibility(GONE);
            }
        }
    }

    public void setOnAddClickedListener(OnAddClickedListener onAddClickedListener) {
        this.onAddClickedListener = onAddClickedListener;
    }

    public void addBitmap(Bitmap bitmap) {
        if (holder != null) {
            holder.images.add(bitmap);
            refreshImage();
        }
    }

    public static class ImageHolder {
        private List<Bitmap> images;

        public ImageHolder(List<Bitmap> images) {
            this.images = images;
        }
    }

    //添加按钮点击了
    public interface OnAddClickedListener {
        public void onAdd(PhotoItem photoItem);
    }
}
