package com.svmuu.common.adapter.box;

import android.content.Context;
import android.text.Html;
import android.view.ViewGroup;

import com.svmuu.R;
import com.svmuu.common.adapter.BaseAdapter;
import com.svmuu.common.entity.TextBoxDetail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TextBoxAdapter extends BaseAdapter<TextBoxDetail,TextBoxHolder>{
    SimpleDateFormat format;
    public TextBoxAdapter(Context context, List<TextBoxDetail> data) {
        super(context, data);
        format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    @Override
    public TextBoxHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TextBoxHolder(getInflater().inflate(R.layout.text_box_item,parent,false));
    }

    @Override
    public void onBindViewHolder(TextBoxHolder holder, int position) {
        TextBoxDetail detail=getData().get(position);
        holder.contentText.setText(Html.fromHtml(detail.content));
        holder.timeText.setText(format.format(new Date(Long.decode(detail.add_time)*1000)));
    }
}
