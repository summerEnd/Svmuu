package com.sp.lib.demo;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.sp.lib.R;
import com.sp.lib.widget.slide.SlideLayout;
import com.sp.lib.widget.slide.transform.FollowTransform;
import com.sp.lib.widget.slide.transform.QQTransform;
import com.sp.lib.widget.slide.transform.Transformer;

/**
 * Created by user1 on 2015/4/14.
 */
public class SlidingTest extends SlibDemoWrapper {
    SlideLayout layout;

    public SlidingTest(SlibDemo demo) {
        super(demo, "sliding menu", "sliding menu");
    }

    @Override
    protected void onCreate() {
        SlibDemo context = getActivity();
        layout = new SlideLayout(context);
        layout.setBackgroundResource(R.drawable.ic_launcher);
        layout.setTransformer(new QQTransform());
        setContentView(layout);

        {
            LinearLayout child = new LinearLayout(context);
            child.setOrientation(LinearLayout.VERTICAL);
            child.addView(createButton(new QQTransform()));
            child.addView(createButton(new FollowTransform(200)));
            layout.addView(child, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        {
            LinearLayout child = new LinearLayout(context);
            child.setBackgroundColor(Color.rgb(150, 150, 150));
            child.setOrientation(LinearLayout.VERTICAL);
            Button open = new Button(context);
            open.setText("open");
            open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout.open();
                }
            });

            Button close = new Button(context);
            close.setText("close");
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout.close();
                }
            });
            child.addView(open);
            child.addView(close);
            layout.addView(child, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    View createButton(final Transformer transformer) {
        Button button = new Button(getActivity());
        button.setText(transformer.getClass().getSimpleName());
        button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setTransformer(transformer);
            }
        });
        return button;
    }
}
