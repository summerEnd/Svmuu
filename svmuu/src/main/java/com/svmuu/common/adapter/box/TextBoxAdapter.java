package com.svmuu.common.adapter.box;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.svmuu.R;
import com.svmuu.common.adapter.BaseAdapter;
import com.svmuu.common.entity.TextBoxDetail;

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

    public TextBoxAdapter(Context context, List<TextBoxDetail> data) {
        super(context, data);
        format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
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
            holder.contentText.setText(Html.fromHtml(detail.content));
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
}
