package com.slib.demo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.sp.lib.widget.slide.menu.MenuDrawer;
import com.sp.lib.widget.slide.menu.Position;
import com.sp.lib.widget.slide.toggle.ArcToggle;
import com.sp.lib.widget.slide.toggle.ArrowToggle;
import com.sp.lib.widget.slide.toggle.FlexibleToggle;
import com.sp.lib.widget.slide.toggle.ToggleRatio;
import com.sp.lib.widget.slide.toggle.shape.SLine;

public class ToggleTest extends SLIBTest implements View.OnClickListener, MenuDrawer.OnDrawerStateChangeListener {
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

        addCustom(new CrossToggle(this), true);
        addCustom(new CrossToggle(this), false);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setBackgroundColor(Color.GRAY);
        mMenuDrawer.setMenuView(frameLayout);
        mMenuDrawer.setMenuSize(300);
        mMenuDrawer.setContentView(container);

        mMenuDrawer.setOnDrawerStateChangeListener(this);
    }

    void addCustom(BaseToggle toggle, boolean rotate) {
        toggle.setPadding(20, 20, 20, 20);
        toggle.setRotate(rotate);
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

    private abstract class BaseToggle extends FlexibleToggle {
        ObjectAnimator run;
        ObjectAnimator reverse;
        boolean isRun;

        public BaseToggle(Context context) {
            super(context);
            run = ObjectAnimator.ofFloat(this, "Ratio", 0, 1).setDuration(250);
            reverse = ObjectAnimator.ofFloat(this, "Ratio", 1, 0).setDuration(250);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isRun) {
                        run.start();
                    } else {
                        reverse.start();
                    }
                    isRun = !isRun;
                }
            });
        }
    }

    private class CrossToggle extends BaseToggle {
        public CrossToggle(Context context) {
            super(context);
        }

        @Override
        protected void onCreateFinalGraphic() {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            int l = getPaddingLeft();
            int r = getPaddingRight();
            int b = getPaddingBottom();
            int t = getPaddingTop();
            setFinalLine(
                    new SLine(width - r, t, l, height - b),
                    new SLine(width / 2, height / 2, width / 2, height / 2),
                    new SLine(width - r, height - b, l, t)
            );
        }
    }
}
