package com.yjy998.ui.pop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupWindow;

import com.yjy998.R;
import com.yjy998.ui.view.RoundButton;

import java.util.ArrayList;

import static android.widget.LinearLayout.LayoutParams;

public class CenterPopup extends PopupWindow implements View.OnClickListener {
    ViewGroup layout;
    PopWidget popWidget;
    private Context context;
    private Listener listener;

    public CenterPopup(Context context) {
        super(context);
        this.context = context;
        View contentView = LayoutInflater.from(context).inflate(R.layout.center_pop_window, null);
        layout = (ViewGroup) contentView.findViewById(R.id.layoutContainer);
        setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#90000000")));
        setContentView(contentView);
        setFocusable(true);
        setAnimationStyle(0);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setPopWidget(PopWidget popWidget) {
        this.popWidget = popWidget;
        layout.removeAllViews();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                context.getResources().getDimensionPixelSize(R.dimen.dimen_pop_btnHeight));
        params.topMargin = context.getResources().getDimensionPixelSize(R.dimen.dimen_15px);
        for (PopItem item : popWidget.widgets) {
            RoundButton button = new RoundButton(context);
            button.setTextSize(15);
            button.setText(item.text);
            button.setTextColor(context.getResources().getColor(R.color.white));
            button.setColor(item.color);
            button.setTag(item);
            button.setOnClickListener(this);
            layout.addView(button, params);
        }
    }

    public PopWidget getPop(){
        return popWidget;
    }

    public void show(View parent) {
        setPopWidget(popWidget);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick((PopItem) v.getTag());
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    public static class PopItem {
        public int id;

        public PopItem(int id, String text, int color) {
            this.id = id;
            this.text = text;
            this.color = color;
        }

        public String text;
        public int color;


    }

    public static class PopWidget {
        ArrayList<PopItem> widgets = new ArrayList<PopItem>();
        public PopItem getById(int id){
            for (PopItem item:widgets){
                if (item.id==id){
                    return item;
                }
            }

            return null;
        }
        public void add(PopItem item) {
            widgets.add(item);
        }

        public int getChildrenCount() {
            return widgets.size();
        }
    }

    public interface Listener {
         void onClick(PopItem item);
    }
}
