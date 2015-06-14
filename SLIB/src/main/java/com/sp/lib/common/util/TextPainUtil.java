package com.sp.lib.common.util;

import android.graphics.Paint;
import android.text.TextPaint;
import android.widget.TextView;

public class TextPainUtil {
    public static void addDeleteLine(TextView tv){
        tv.getPaint().setFlags(TextPaint.STRIKE_THRU_TEXT_FLAG);
    }
    public static void addUnderLine(TextView tv){
        tv.getPaint().setFlags(TextPaint.UNDERLINE_TEXT_FLAG);
    }
    public static float getBaseLineOffset(Paint p){

        return -(p.ascent()+p.descent())/2;
    }
}
