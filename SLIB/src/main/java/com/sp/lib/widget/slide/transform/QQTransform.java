package com.sp.lib.widget.slide.transform;

import android.view.View;

public class QQTransform implements Transformer {
    float followWidth=220;
    float mainRemain=100;
    float minScale=0.5f;

    /**
     * @param followWidth the follow width
     * @param mainRemain when the menu is open ,the width that the main layout left
     * @param minScale the final scale,for main layout when the slide is open,for menu layout when the slide is closed
     */
    public QQTransform(float followWidth, float mainRemain, float minScale) {
        this.followWidth = followWidth;
        this.mainRemain = mainRemain;
        this.minScale = minScale;
    }

    @Override
    public void transform(View main, View menu, float delta) {
        menu.setTranslationX(followWidth * delta - followWidth);
        menu.setAlpha(Math.max(0.5f, delta));

        float menuFactor=minScale*delta+minScale;
        menu.setScaleX(menuFactor);
        menu.setScaleY(menuFactor);

        float mainFactor=-0.5f*delta+1;
        main.setScaleX(mainFactor);
        main.setScaleY(mainFactor);

        main.setTranslationX(-mainRemain*delta);
    }
}
