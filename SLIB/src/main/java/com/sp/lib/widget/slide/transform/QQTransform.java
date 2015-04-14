package com.sp.lib.widget.slide.transform;

import android.view.View;

public class QQTransform implements Transformer {
    float followWidth=220;
    float mainRemain=100;
    float minScale=0.5f;
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
