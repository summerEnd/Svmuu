package com.svmuu.common.adapter.box;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.svmuu.R;
import com.svmuu.common.ImageOptions;
import com.svmuu.common.adapter.BaseAdapter;
import com.svmuu.common.entity.TextBoxDetail;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TextBoxAdapter extends BaseAdapter<TextBoxDetail, TextBoxHolder> {
    SimpleDateFormat format;

    private String title;
    private String summary;
    private TextView titleText;
    private TextView summaryText;
    private Html.ImageGetter imageGetter;
    public TextBoxAdapter(Context context, List<TextBoxDetail> data) {
        super(context, data);
        format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                File file = ImageLoader.getInstance().getDiskCache().get(source);
                if (file != null && file.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(getContext().getResources(), bitmap);
                    bitmapDrawable.setBounds(0, 0, 40, 40);
                    return bitmapDrawable;
                }
                ImageLoader.getInstance().loadImage(source, ImageOptions.getStandard(), listener);
                return null;
            }
        };
    }

    public void setHeadInfo(String title,String summary){
        this.title=title;
        this.summary=summary;
    }

    @Override
    public TextBoxHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View itemView = getInflater().inflate(R.layout.box_summary_layout, parent, false);
            titleText = (TextView) itemView.findViewById(R.id.titleText);
            summaryText = (TextView) itemView.findViewById(R.id.summaryText);
            titleText.setText(title);
            summaryText.setText(summary);
            return new TextBoxHolder(itemView);
        }
        return new TextBoxHolder(getInflater().inflate(R.layout.text_box_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TextBoxHolder holder, int position) {
        if (position != 0) {
            TextBoxDetail detail = getData().get(position-1);
            holder.contentText.setText(Html.fromHtml(detail.content,imageGetter,null));
            holder.timeText.setText(format.format(new Date(Long.decode(detail.add_time) * 1000)));
        }else{
            titleText.setText(title);
            summaryText.setText(summary);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }
    private ImageLoadingListener listener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String s, View view) {

        }

        @Override
        public void onLoadingFailed(String s, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            notifyDataSetChanged();
        }

        @Override
        public void onLoadingCancelled(String s, View view) {

        }
    };
}
