package com.yjy998.ui.pop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.yjy998.R;

import java.util.ArrayList;

public class CenterPopup extends PopupWindow implements View.OnClickListener {
    ViewGroup layout;
    PopWidget popWidget;
    private Context context;
    private Listener listener;

    public CenterPopup(Context context) {
        super(context);
        this.context = context;
        layout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.center_pop_window, null);
        setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(layout);
        setFocusable(true);
    }

    public void setPopWidget(PopWidget popWidget) {
        this.popWidget = popWidget;
        layout.removeAllViews();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 12;
        lp.leftMargin = 40;
        lp.rightMargin = 40;
        for (PopItem item : popWidget.widgets) {
            Button button = new Button(context);
            button.setText(item.text);
            button.setTextColor(context.getResources().getColor(R.color.white));
            button.setBackgroundResource(item.drawableId);
            button.setTag(item);
            button.setOnClickListener(this);
            layout.addView(button, lp);
        }
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

        public PopItem(int id, String text, int drawableId) {
            this.id = id;
            this.text = text;
            this.drawableId = drawableId;
        }

        public String text;
        public int drawableId;


    }

    public static class PopWidget {
        ArrayList<PopItem> widgets = new ArrayList<PopItem>();

        public void add(PopItem item) {
            widgets.add(item);
        }

        public int getChildrenCount() {
            return widgets.size();
        }
    }

    public interface Listener {
        public void onClick(PopItem item);
    }
}
