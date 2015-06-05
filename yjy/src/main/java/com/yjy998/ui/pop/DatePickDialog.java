package com.yjy998.ui.pop;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.yjy998.R;

import java.util.Calendar;

public class DatePickDialog extends Dialog implements View.OnClickListener {

    private DatePicker datePicker;
    OnDatePicked onDatePicked;

    public DatePickDialog(Context context,OnDatePicked onDatePicked) {
        super(context);
        this.onDatePicked=onDatePicked;
        setContentView(R.layout.date_pick_dialog);
        initialize();
    }

    private void initialize() {

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();

        switch (v.getId()) {
            case R.id.cancel: {
                break;
            }
            case R.id.confirm: {
                Calendar cal = Calendar.getInstance();
                cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                onDatePicked.onPick(cal);
                break;
            }
        }
    }

    public interface OnDatePicked {
        public void onPick(Calendar cal);
    }
}
