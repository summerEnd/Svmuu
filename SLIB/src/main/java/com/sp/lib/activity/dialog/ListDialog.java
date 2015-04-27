package com.sp.lib.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static android.widget.AdapterView.OnItemClickListener;

public class ListDialog extends Dialog implements OnItemClickListener{
    CharSequence[] items;
    int textResId;
    OnItemClickListener onItemClickListener;
    public ListDialog(Context context, CharSequence[] items) {
        super(context);
        this.items = items;
    }

    public ListDialog(Context context, int itemsId) {
        this(context, context.getResources().getStringArray(itemsId));
    }

    public ListDialog(Context context, CharSequence[] items, int textResId) {
        this(context, items);
        this.textResId = textResId;
    }

    public ListDialog(Context context, int itemsId, int textResId) {
        this(context, context.getResources().getStringArray(itemsId));
        this.textResId = textResId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = new ListView(getContext());
        if (textResId == 0) {
            textResId = android.R.layout.simple_list_item_1;
        }
        listView.setAdapter(new ArrayAdapter<CharSequence>(getContext(), textResId, items));
        listView.setOnItemClickListener(this);
        setContentView(listView);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
     if (onItemClickListener!=null){
         onItemClickListener.onItemClick(parent,view,position,id);
     }
    }
}
