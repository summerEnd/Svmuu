package com.slib.demo;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.sp.lib.widget.slide.menu.MenuDrawer;
import com.sp.lib.widget.slide.menu.Position;
import com.sp.lib.widget.slide.toggle.ArcToggle;
import com.sp.lib.widget.slide.toggle.ArrowToggle;
import com.sp.lib.widget.slide.toggle.FlexibleToggle;
import com.sp.lib.widget.slide.toggle.ToggleRatio;
import com.sp.lib.widget.slide.toggle.shape.SLine;

public class ToggleTest extends SLIBTest implements View.OnClickListener {
    MenuDrawer mMenuDrawer;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.START, MenuDrawer.MENU_DRAG_CONTENT);

        container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        addToggle(new ArrowToggle(this));
        ArcToggle toggle = new ArcToggle(this);
        toggle.setUseCenter(true);
        toggle.getPaint().setStyle(Paint.Style.STROKE);
        addToggle(toggle);
        ArcToggle toggle1 = new ArcToggle(this);
        toggle1.setUseCenter(false);
        toggle1.getPaint().setStyle(Paint.Style.STROKE);

        addToggle(toggle1);

        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setBackgroundColor(Color.GRAY);
        mMenuDrawer.setMenuView(frameLayout);
        mMenuDrawer.setMenuSize(300);
        mMenuDrawer.setContentView(container);

        mMenuDrawer.setOnDrawerStateChangeListener(new MenuDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {

            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                int count = container.getChildCount();
                for (int j = 0; j < count; j++) {
                    ((ToggleRatio) container.getChildAt(j)).setRatio(openRatio);
                }
            }
        });
        add(container);
        add2(container);
    }

    void add(ViewGroup container) {
        final FlexibleToggle toggle = new FlexibleToggle(this);
        toggle.setPadding(20, 20, 20, 20);
        toggle.setRotate(true);
        toggle.setOnClickListener(new View.OnClickListener() {
            private boolean is;

            @Override
            public void onClick(View v) {
                cross();
            }

            void cross() {
                int width = toggle.getWidth();
                int height = toggle.getHeight();
                int l = toggle.getPaddingLeft();
                int r = toggle.getPaddingRight();
                int b = toggle.getPaddingBottom();
                int t = toggle.getPaddingTop();
                toggle.setFinalLine(
                        new SLine(l, height - b, width - r, t),
                        new SLine(width / 2, height / 2, width / 2, height / 2),
                        new SLine(l, t, width - r, height - b)
                );
                if (is) {
                    reverse();
                } else {
                    start();
                }
            }

            void start() {
                is = true;
                ObjectAnimator.ofFloat(toggle, "Ratio", 0, 1).setDuration(500).start();
            }

            void reverse() {
                is = false;
                ObjectAnimator.ofFloat(toggle, "Ratio", 1, 0).setDuration(500).start();
            }
        });
        container.addView(toggle, new LinearLayout.LayoutParams(80, 80));
    }

    void add2(ViewGroup container) {
        final FlexibleToggle toggle = new FlexibleToggle(this);
        toggle.setPadding(20, 20, 20, 20);
        toggle.setRotate(false);
        toggle.setOnClickListener(new View.OnClickListener() {
            private boolean is;

            @Override
            public void onClick(View v) {
                cross();
            }

            void cross() {
                int width = toggle.getWidth();
                int height = toggle.getHeight();
                int l = toggle.getPaddingLeft();
                int r = toggle.getPaddingRight();
                int b = toggle.getPaddingBottom();
                int t = toggle.getPaddingTop();
                toggle.setFinalLine(
                        new SLine(width - r, t, l, height - b),
                        new SLine(width / 2, height / 2, width / 2, height / 2),
                        new SLine(width - r, height - b, l, t)
                );
                if (is) {
                    reverse();
                } else {
                    start();
                }
            }

            void start() {
                is = true;
                ObjectAnimator.ofFloat(toggle, "Ratio", 0, 1).setDuration(250).start();
            }

            void reverse() {
                is = false;
                ObjectAnimator.ofFloat(toggle, "Ratio", 1, 0).setDuration(250).start();
            }
        });
        container.addView(toggle, new LinearLayout.LayoutParams(80, 80));
    }


    void addToggle(View toggle) {
        toggle.setPadding(15, 20, 15, 20);
        container.addView(toggle, new LinearLayout.LayoutParams(80, 80));
        toggle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mMenuDrawer.isMenuVisible()) {
            mMenuDrawer.closeMenu();
        } else {
            mMenuDrawer.openMenu();
        }
    }
}
