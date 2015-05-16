package com.yjy998.ui.pop;


import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.NumberPicker;

import com.yjy998.R;
import com.yjy998.common.Constant;

import java.util.Arrays;

public class PickCity extends Dialog implements View.OnClickListener {
    private NumberPicker provincePicker;
    private NumberPicker cityPicker;
    private OnSelectListener onSelectListener;
    private String provinces[];
    int scrollMode;

    public PickCity(Context context) {
        super(context);
        setContentView(R.layout.city_picker);
        initialize();
    }

    private void initialize() {
        findViewById(R.id.buttonYes).setOnClickListener(this);
        findViewById(R.id.buttonNo).setOnClickListener(this);
        provincePicker = (NumberPicker) findViewById(R.id.province);
        cityPicker = (NumberPicker) findViewById(R.id.city);
        cityPicker.setWrapSelectorWheel(false);
        displayProvince();
    }

    void displayProvince() {
        provinces = new String[Constant.citys.length];
        int cityLength = 0;
        for (int i = 0; i < provinces.length; i++) {
            provinces[i] = Constant.citys[i][0];
            cityLength = Math.max(Constant.citys[i].length, cityLength);
        }
        String[] citiesFixed = new String[cityLength];
        String[] cities = Constant.citys[0];
        for (int i = 0; i < citiesFixed.length; i++) {
            if (i < cities.length - 1) {
                //第一个是省
                citiesFixed[i] = cities[i + 1];
            } else {
                citiesFixed[i] = "";
            }

        }

        provincePicker.setMaxValue(provinces.length - 1);
        provincePicker.setMinValue(0);
        provincePicker.setDisplayedValues(provinces);
        cityPicker.setMinValue(0);
        cityPicker.setMaxValue(cityLength - 1);
        cityPicker.setDisplayedValues(citiesFixed);


        provincePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                String cities[] = Constant.citys[newVal];
                if (cities.length > 1) {
                    String[] strings = Arrays.copyOfRange(cities, 1, cities.length);
                    setCities(strings);
                } else {
                    setCities(new String[]{"", ""});
                }
            }
        });
    }

    void setCities(String[] cities) {
        String displayValues[]=cityPicker.getDisplayedValues();
        int length = cities.length;
        if (length < displayValues.length) {
            for (int i = 0; i < displayValues.length; i++) {
                if (i < length) {
                    displayValues[i] = cities[i];
                } else {
                    displayValues[i] = "";
                }
            }

        }
        cityPicker.setMinValue(0);
        cityPicker.setValue(0);
        cityPicker.setDisplayedValues(displayValues);
        cityPicker.setMaxValue(cities.length-1);
        cityPicker.invalidate();
    }

    public PickCity setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
        return this;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.buttonYes:
                if (onSelectListener != null) {
                    onSelectListener.onSelect(provincePicker.getDisplayedValues()[provincePicker.getValue()], cityPicker.getDisplayedValues()[cityPicker.getValue()]);
                }
                break;
            case R.id.buttonNo:
                break;
        }
    }

    public interface OnSelectListener {
        public void onSelect(String province, String city);
    }
}
