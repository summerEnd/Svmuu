package com.yjy998.ui.pop;


import android.app.Dialog;
import android.content.Context;
import android.widget.NumberPicker;

import com.yjy998.R;

public class PickCity extends Dialog {
    private NumberPicker province;
    private NumberPicker city;

    public PickCity(Context context) {
        super(context);
        setContentView(R.layout.city_picker);
        initialize();
    }

    private void initialize() {

        province = (NumberPicker) findViewById(R.id.province);
        city = (NumberPicker) findViewById(R.id.city);
        final String[] displayedValues = getContext().getResources().getStringArray(R.array.provinces);
        province.setMaxValue(displayedValues.length - 1);
        province.setMinValue(0);
        province.setDisplayedValues(displayedValues);
        setCities(displayedValues[province.getValue()]);
        province.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                setCities(displayedValues[newVal]);
            }
        });
    }

    void setCities(String province) {
        String[] values = new String[20];
        for (int i = 0; i < values.length; i++) {
            values[i] = province + i;
        }
        city.setDisplayedValues(values);
        city.setMinValue(0);
        city.setMaxValue(values.length - 1);
        city.invalidate();
    }
}
