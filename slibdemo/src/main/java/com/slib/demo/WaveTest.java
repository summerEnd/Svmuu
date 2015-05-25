package com.slib.demo;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sp.lib.widget.WaveWindow;

public class WaveTest extends SLIBTest {

    private View.OnClickListener l;
    private WaveWindow waveWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout);
        waveWindow = new WaveWindow(this);

        layout.addView(create(Gravity.TOP, "TOP"));
        layout.addView(create(Gravity.BOTTOM, "BOTTOM"));
        layout.addView(create(Gravity.LEFT, "LEFT"));
        layout.addView(create(Gravity.RIGHT, "RIGHT"));

        final Button button = new Button(this);
        button.setText("Button作为Parent");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waveWindow.dismiss();
                waveWindow = new WaveWindow(button.getContext());
            }
        });
        layout.addView(button);
        layout.addView(createShowAs(Gravity.TOP, "TOP show as"));
        layout.addView(createShowAs(Gravity.BOTTOM, "BOTTOM show as"));
        layout.addView(createShowAs(Gravity.LEFT, "LEFT show as"));
        layout.addView(createShowAs(Gravity.RIGHT, "RIGHT show as"));
    }

    Button create(int gravity, String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setTag(gravity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waveWindow.dismiss();
                int gravity = (Integer) v.getTag();
                waveWindow.showAtLocation(v, gravity, 0, 0);

            }
        });
        return button;
    }

    Button createShowAs(int gravity, String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setTag(gravity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waveWindow.dismiss();
                waveWindow.showAsDropDown(v, 0, 0);
            }
        });
        return button;
    }
}
