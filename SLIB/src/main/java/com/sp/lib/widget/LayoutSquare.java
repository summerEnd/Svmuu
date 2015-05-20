package com.sp.lib.widget;

import static android.view.View.MeasureSpec;


public class LayoutSquare {
    public static final int NONE = 0;
    public static final int WIDTH = 1;
    public static final int HEIGHT = 2;
    public static final int SHORTER = 3;
    public static final int LONGER = 4;

    public static  int apply(int squareBy, int widthMeasureSpec, int heightMeasureSpec) {
        int squareSpec;
        switch (squareBy) {
            case WIDTH:
                squareSpec = widthMeasureSpec;
                break;
            case HEIGHT:
                squareSpec = heightMeasureSpec;
                break;
            case LONGER: {
                int widthSize = MeasureSpec.getSize(widthMeasureSpec);
                int heightSize = MeasureSpec.getSize(heightMeasureSpec);
                int mode;
                int size;
                if (widthSize > heightSize) {
                    size = widthSize;
                    mode = MeasureSpec.getMode(widthSize);
                } else {
                    size = heightSize;
                    mode = MeasureSpec.getMode(heightSize);
                }
                squareSpec = MeasureSpec.makeMeasureSpec(size, mode);
            }
            break;
            case SHORTER: {
                int widthSize = MeasureSpec.getSize(widthMeasureSpec);
                int heightSize = MeasureSpec.getSize(heightMeasureSpec);
                int mode;
                int size;
                if (widthSize < heightSize) {
                    size = widthSize;
                    mode = MeasureSpec.getMode(widthSize);
                } else {
                    size = heightSize;
                    mode = MeasureSpec.getMode(heightSize);
                }
                squareSpec = MeasureSpec.makeMeasureSpec(size, mode);
            }
            break;
            default:
                squareSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                break;
        }
        return squareSpec;
    }
}
