package com.slib.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sp.lib.widget.AutoLayout;
import com.svmuu.slibdemo.R;


public class AutoLayoutTest extends Activity {
    private EditText editText;
    private AutoLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_layout_test);
        editText = (EditText) findViewById(R.id.edit);
        layout = (AutoLayout) findViewById(R.id.autoLayout);
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButton();
            }
        });
    }

    void addButton() {
        String text = editText.getText().toString();
        editText.setText(null);
        Button button = new Button(this);
        button.setText(text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.removeView(view);
            }
        });
        layout.addView(button, new AutoLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

}
