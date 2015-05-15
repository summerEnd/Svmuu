package com.yjy998.ui.activity.main.my.business.capital;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yjy998.R;

public class TrendInfoWindow extends PopupWindow {
    private TextView newPriceText;
    private TextView averagePriceText;
    private TextView timeText;
    private Context context;
    private boolean isLeft = true;

    public TrendInfoWindow(Context context) {
        super(context);
        this.context = context;
        setAnimationStyle(0);
        View view = LayoutInflater.from(context).inflate(R.layout.stock_info_pop, null);
        setContentView(view);
        newPriceText = (TextView) view.findViewById(R.id.newPriceText);
        averagePriceText = (TextView) view.findViewById(R.id.averagePriceText);
        timeText = (TextView) view.findViewById(R.id.timeText);

        setWindowLayoutMode(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void setText(String newPrice, String average, String time) {
        newPriceText.setText(context.getString(R.string.new_price_s, newPrice));
        averagePriceText.setText(context.getString(R.string.average_price_s, average));
        timeText.setText(context.getString(R.string.time_s,time));
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void show(View parent, boolean left) {

        if (this.isLeft != left || !isShowing()) {
            int gravity = Gravity.CENTER_VERTICAL;
            if (left) {
                gravity |= Gravity.LEFT;
            } else {
                gravity |= Gravity.RIGHT;
            }

            if (isShowing()) {
                dismiss();
            }

            showAtLocation(parent, gravity, 0, 0);
        }
    }

}
