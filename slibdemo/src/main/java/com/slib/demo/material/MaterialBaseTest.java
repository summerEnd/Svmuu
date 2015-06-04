package com.slib.demo.material;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.slib.demo.SLIBTest;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.widget.material.MaterialLayout;


public class MaterialBaseTest extends SLIBTest {

    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrollView scrollView = new ScrollView(this);
        container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(container);
        setContentView(scrollView);

        MaterialLayout layout = new MaterialLayout(this);
        container.addView(layout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80));
        layout.setMaterialBackground(Color.GRAY);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContextUtil.toast("hello::");
            }
        });
        addTextView("Hello!");
        addTextView("Hello Material!");
    }

    void addTextView(final String text) {
        MaterialLayout layout = new MaterialLayout(this);
        TextView textView = new TextView(this);
        layout.addView(textView);
        textView.setText(text);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin=10;
        params.topMargin=10;
        params.rightMargin=10;
        params.bottomMargin=10;
        container.addView(layout);
        layout.setMaterialBackground(Color.WHITE);
        layout.setMaterialWave(Color.LTGRAY);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContextUtil.toast("hello::"+text);
            }
        });
    }

}
